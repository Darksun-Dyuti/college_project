"""
Train insurance models using India-labeled public datasets and save artifacts
compatible with the Spring Boot prediction pipeline.
"""

from __future__ import annotations

import json
import os
from pathlib import Path

import joblib
import numpy as np
import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder
from sklearn.tree import DecisionTreeRegressor


DATASETS = {
    "numasive_indian_health": "https://raw.githubusercontent.com/Numasive/Indian-Health-Insurance-Dataset-Analysis/main/insurance.csv",
    "ybi_indian_premium": "https://raw.githubusercontent.com/ybifoundation/Dataset/main/Insurance%20Premium.csv",
}

LOCATION_MAP = {
    "east": "southeast",
    "west": "northwest",
    "south": "southwest",
    "north": "northeast",
}

MODEL_DIR = Path("src/main/resources/models")
DATA_DIR = Path("data")


def download_datasets() -> list[Path]:
    DATA_DIR.mkdir(exist_ok=True)
    downloaded = []

    for name, url in DATASETS.items():
        out_path = DATA_DIR / f"{name}.csv"
        df = pd.read_csv(url)
        df.to_csv(out_path, index=False)
        downloaded.append(out_path)
        print(f"[+] Downloaded {name}: {out_path} ({len(df)} rows)")

    return downloaded


def normalize_dataframe(df: pd.DataFrame) -> pd.DataFrame:
    normalized = df.copy()
    normalized.columns = [c.strip().lower() for c in normalized.columns]

    rename_map = {
        "sex": "gender",
        "children": "kids",
        "region": "location",
        "premium": "cost",
        "charges": "cost",
    }
    normalized = normalized.rename(columns=rename_map)

    required_base = ["age", "gender", "bmi", "kids", "smoker", "location", "cost"]
    missing = [c for c in required_base if c not in normalized.columns]
    if missing:
        raise ValueError(f"Missing required columns after normalization: {missing}")

    normalized["gender"] = normalized["gender"].astype(str).str.lower().str.strip()
    normalized["smoker"] = normalized["smoker"].astype(str).str.lower().str.strip()
    normalized["location"] = normalized["location"].astype(str).str.lower().str.strip().replace(LOCATION_MAP)

    normalized["smoker"] = normalized["smoker"].replace({"true": "yes", "false": "no", "1": "yes", "0": "no"})

    # Build extended features expected by the app's prediction payload.
    normalized["annual_income"] = (normalized["cost"] * 14).clip(lower=120000, upper=5000000)
    normalized["income"] = pd.cut(
        normalized["annual_income"],
        bins=[-1, 300000, 1000000, 2500000, float("inf")],
        labels=["low", "medium", "high", "very_high"],
    ).astype(str)

    rng = np.random.default_rng(42)
    normalized["employment"] = rng.choice(
        ["employed", "self_employed", "unemployed", "retired"],
        size=len(normalized),
        p=[0.62, 0.2, 0.1, 0.08],
    )

    health_base = 8.5 - (normalized["bmi"] - 24).abs() / 6 - (normalized["smoker"] == "yes").astype(int) * 1.8
    normalized["health_score"] = np.clip(np.round(health_base + rng.normal(0, 0.8, len(normalized))), 1, 10).astype(int)

    normalized["exercise_frequency"] = np.clip(
        np.round(5 - (normalized["bmi"] - 24).abs() / 5 + rng.normal(0, 1, len(normalized))),
        0,
        7,
    ).astype(int)

    normalized["education"] = rng.choice(
        ["high_school", "bachelor", "master", "phd"],
        size=len(normalized),
        p=[0.28, 0.44, 0.22, 0.06],
    )
    normalized["marital"] = rng.choice(
        ["single", "married", "divorced", "widowed"],
        size=len(normalized),
        p=[0.31, 0.56, 0.09, 0.04],
    )
    normalized["years_insured"] = np.clip(normalized["age"] - 18 + rng.integers(-3, 4, size=len(normalized)), 0, 50).astype(int)

    for col in ["age", "bmi", "kids", "health_score", "exercise_frequency", "years_insured", "cost"]:
        normalized[col] = pd.to_numeric(normalized[col], errors="coerce")

    normalized = normalized.dropna(subset=["age", "gender", "bmi", "kids", "smoker", "location", "cost"])
    normalized = normalized.reset_index(drop=True)
    return normalized


def prepare_training_data(df: pd.DataFrame):
    le_gender = LabelEncoder()
    le_location = LabelEncoder()
    le_income = LabelEncoder()
    le_employment = LabelEncoder()
    le_education = LabelEncoder()
    le_marital = LabelEncoder()

    df["gender_encoded"] = le_gender.fit_transform(df["gender"])
    df["location_encoded"] = le_location.fit_transform(df["location"])
    df["income_encoded"] = le_income.fit_transform(df["income"])
    df["employment_encoded"] = le_employment.fit_transform(df["employment"])
    df["education_encoded"] = le_education.fit_transform(df["education"])
    df["marital_encoded"] = le_marital.fit_transform(df["marital"])
    df["smoker_int"] = (df["smoker"] == "yes").astype(int)

    feature_names = [
        "age",
        "gender_encoded",
        "bmi",
        "kids",
        "smoker_int",
        "location_encoded",
        "income_encoded",
        "employment_encoded",
        "health_score",
        "exercise_frequency",
        "education_encoded",
        "marital_encoded",
        "years_insured",
    ]

    X = df[feature_names]
    y = df["cost"]

    encoders = {
        "gender": le_gender,
        "location": le_location,
        "income": le_income,
        "employment": le_employment,
        "education": le_education,
        "marital": le_marital,
    }

    return X, y, feature_names, encoders


def evaluate(y_train, y_train_pred, y_test, y_test_pred):
    return {
        "train_r2": float(r2_score(y_train, y_train_pred)),
        "test_r2": float(r2_score(y_test, y_test_pred)),
        "train_rmse": float(np.sqrt(mean_squared_error(y_train, y_train_pred))),
        "test_rmse": float(np.sqrt(mean_squared_error(y_test, y_test_pred))),
        "train_mae": float(mean_absolute_error(y_train, y_train_pred)),
        "test_mae": float(mean_absolute_error(y_test, y_test_pred)),
    }


def train_and_save(df: pd.DataFrame):
    MODEL_DIR.mkdir(parents=True, exist_ok=True)

    X, y, feature_names, encoders = prepare_training_data(df)
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    models = {
        "random_forest": RandomForestRegressor(n_estimators=150, max_depth=12, random_state=42),
        "decision_tree": DecisionTreeRegressor(max_depth=10, random_state=42),
        "linear_regression": LinearRegression(),
    }

    metrics = {}
    for name, model in models.items():
        model.fit(X_train, y_train)
        train_pred = model.predict(X_train)
        test_pred = model.predict(X_test)
        metrics[name] = evaluate(y_train, train_pred, y_test, test_pred)
        print(
            f"[+] {name}: test_r2={metrics[name]['test_r2']:.4f}, "
            f"test_rmse={metrics[name]['test_rmse']:.2f}, test_mae={metrics[name]['test_mae']:.2f}"
        )

    joblib.dump(models["random_forest"], MODEL_DIR / "insurance_model.pkl")
    joblib.dump(models["decision_tree"], MODEL_DIR / "insurance_model_decision_tree.pkl")
    joblib.dump(models["linear_regression"], MODEL_DIR / "insurance_model_linear_regression.pkl")

    for key, enc in encoders.items():
        joblib.dump(enc, MODEL_DIR / f"label_encoder_{key}.pkl")

    config = {
        "models": ["random_forest", "decision_tree", "linear_regression"],
        "default_model": "random_forest",
        "feature_names": feature_names,
        "encoders": list(encoders.keys()),
        "metrics": metrics,
        "training_data": {
            "source": "india_labeled_public_csv",
            "datasets": list(DATASETS.keys()),
            "row_count": int(len(df)),
        },
    }

    with open(MODEL_DIR / "model_config.json", "w", encoding="utf-8") as f:
        json.dump(config, f, indent=2)

    print(f"[+] Saved model artifacts to {MODEL_DIR}")


def main():
    files = download_datasets()
    frames = [normalize_dataframe(pd.read_csv(path)) for path in files]
    merged = pd.concat(frames, ignore_index=True).drop_duplicates().reset_index(drop=True)

    print(f"[+] Combined normalized rows: {len(merged)}")
    print("[+] Location classes:", sorted(merged["location"].unique().tolist()))
    print("[+] Smoker classes:", sorted(merged["smoker"].unique().tolist()))

    train_and_save(merged)


if __name__ == "__main__":
    main()
