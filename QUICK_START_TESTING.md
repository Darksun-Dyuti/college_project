# 🚀 Quick Start Guide - Testing New Features

## What's New?

Your insurance prediction system now includes **7 new comprehensive features**:

1. **Income Level** - Economic status assessment
2. **Employment Status** - Job stability indicator
3. **Health Score** - Overall health condition (1-10)
4. **Exercise Frequency** - Weekly exercise days (0-7)
5. **Education Level** - Highest education attained
6. **Marital Status** - Current relationship status
7. **Years Insured** - Insurance history length

---

## 📝 Step 1: Train the Model with New Features

This MUST be done first before making predictions!

```bash
# Navigate to project root
cd c:\Users\ASIF EBRAHIM\Downloads\demo

# Activate virtual environment (already done)
# .venv\Scripts\Activate.ps1

# Train the model with all 13 features
python train_model.py
```

**Expected Output**:
```
Dataset shape: (500, 14)
First 5 rows:
...
[1] Training Random Forest...
    Train R²: 0.8245, Test R²: 0.8012
    ...
[2] Training Decision Tree...
[3] Training Linear Regression...

[+] Gender encoder saved to: src/main/resources/models/label_encoder_gender.pkl
[+] Location encoder saved to: src/main/resources/models/label_encoder_location.pkl
[+] Income encoder saved to: src/main/resources/models/label_encoder_income.pkl
[+] Employment encoder saved to: src/main/resources/models/label_encoder_employment.pkl
[+] Education encoder saved to: src/main/resources/models/label_encoder_education.pkl
[+] Marital Status encoder saved to: src/main/resources/models/label_encoder_marital.pkl
[+] Configuration saved to: src/main/resources/models/model_config.json
```

✅ **Verify**: Check that 9 encoders are created in `src/main/resources/models/`

---

## 🔧 Step 2: Build and Run the Application

```bash
# Build the project
mvn clean install

# Run the Spring Boot application
mvn spring-boot:run
```

**Expected Output**:
```
... 
2026-02-22 14:35:42.123  INFO ... Started CostPredictionApplication in 3.456 seconds
```

✅ **Verify**: Navigate to `http://localhost:8080` - You should see the home page

---

## 🌐 Step 3: Test via Web Form (Recommended)

### Option A: Simple Test (with defaults)
1. Go to `http://localhost:8080`
2. Click login/register if needed
3. Fill in ONLY these fields:
   - **Name**: John Doe
   - **Age**: 45
   - **Gender**: Male
   - **BMI**: 28.5
   - **Region**: Northeast
4. Leave other fields as defaults (they have smart defaults)
5. Click "Get Estimate"

**Expected Result**:
```
=== Insurance Cost Estimate ===
Model Used: RANDOM FOREST
Estimated Annual Cost: $7,250.34

=== Your Profile ===
Age: 45 | BMI: 28.5 | Gender: male
Smoker: No | Children: 0 | Location: northeast
Income: medium | Employment: employed
Health Score: 7/10 | Exercise: 3 days/week | Education: bachelor
Marital Status: single | Years Insured: 0

=== Personalized Suggestions to Reduce Cost ===
...
```

### Option B: Full Test (All Fields)
1. Go to `http://localhost:8080`
2. Fill in ALL fields:

**Example Data**:
```
Basic Information:
  Name: Sarah Johnson
  Age: 38
  Gender: Female
  BMI: 24.5
  Number of Kids: 2
  Smoker: No
  Region: Northeast
  
Additional Health & Lifestyle Info:
  Income Level: Very High
  Employment Status: Employed
  Health Score: 9
  Exercise Frequency: 6 days/week
  Education Level: Master's Degree
  Marital Status: Married
  Years Insured: 8
  
Model Selection: Random Forest
```

3. Click "Get Estimate"

**Expected Result**: Lower cost due to excellent health profile

---

## 🐍 Step 4: Test via Python Script (Direct)

Test the prediction script directly:

### Test 1: Low-Risk Profile
```bash
python src/main/resources/predict_model.py ^
  age=30 gender=female bmi=22 kids=1 smoker=no ^
  location=northeast income=very_high employment=employed ^
  health_score=9 exercise_frequency=6 education=phd ^
  marital_status=married years_insured=8 --model random_forest
```

**Expected Output**:
```json
{"model": "random_forest", "prediction": 3850.25}
```

### Test 2: High-Risk Profile
```bash
python src/main/resources/predict_model.py ^
  age=65 gender=male bmi=38 kids=4 smoker=yes ^
  location=southwest income=low employment=retired ^
  health_score=3 exercise_frequency=0 education=high_school ^
  marital_status=divorced years_insured=0 --model random_forest
```

**Expected Output**:
```json
{"model": "random_forest", "prediction": 14250.75}
```

### Test 3: Medium-Risk Profile
```bash
python src/main/resources/predict_model.py ^
  age=45 gender=male bmi=28.5 kids=2 smoker=no ^
  location=southeast income=medium employment=employed ^
  health_score=7 exercise_frequency=3 education=bachelor ^
  marital_status=married years_insured=10 --model random_forest
```

**Expected Output**:
```json
{"model": "random_forest", "prediction": 7500.50}
```

---

## ✅ Verification Checklist

### Model Training
- [ ] All 3 models trained (Random Forest, Decision Tree, Linear Regression)
- [ ] All 9 encoders created in `src/main/resources/models/`
- [ ] `model_config.json` updated with 13 features
- [ ] No errors during training

### Application
- [ ] Application starts without errors
- [ ] http://localhost:8080 loads successfully
- [ ] Form displays all 7 new fields

### Frontend Form
- [ ] ✅ Income Level dropdown (4 options)
- [ ] ✅ Employment Status dropdown (4 options)
- [ ] ✅ Health Score input (1-10)
- [ ] ✅ Exercise Frequency input (0-7)
- [ ] ✅ Education Level dropdown (4 options)
- [ ] ✅ Marital Status dropdown (4 options)
- [ ] ✅ Years Insured input (0-50)

### API Endpoint
- [ ] POST /api/predict accepts 14 fields
- [ ] Returns prediction with profile summary
- [ ] Validation works for all fields
- [ ] Default values apply correctly

### Python Script
- [ ] Accepts key=value format
- [ ] Still accepts legacy positional format
- [ ] Loads all 9 encoders
- [ ] Outputs JSON prediction
- [ ] Works with --model parameter

---

## 🧪 Sample Test Payloads

### Test Payload 1 (JSON for API)
```json
{
  "name": "Alice Williams",
  "age": 35,
  "gender": "female",
  "bmi": 23.5,
  "kids": 1,
  "smoker": false,
  "location": "northeast",
  "existingCondition": "None",
  "model": "random_forest",
  "income": "high",
  "employment": "employed",
  "healthScore": 8,
  "exerciseFrequency": 5,
  "education": "master",
  "maritalStatus": "married",
  "yearsInsured": 5
}
```

### Test Payload 2 (Different Model)
```json
{
  "name": "Bob Smith",
  "age": 55,
  "gender": "male",
  "bmi": 31.2,
  "kids": 3,
  "smoker": true,
  "location": "southwest",
  "existingCondition": "High Blood Pressure",
  "model": "decision_tree",
  "income": "medium",
  "employment": "self_employed",
  "healthScore": 5,
  "exerciseFrequency": 2,
  "education": "bachelor",
  "maritalStatus": "married",
  "yearsInsured": 15
}
```

---

## 🐛 Troubleshooting

### Issue: "Model not found" error
**Solution**: Run `python train_model.py` first to create all model files

### Issue: "Label encoder not found"
**Solution**: Make sure all 9 `.pkl` files exist in `src/main/resources/models/`

### Issue: "Invalid value for field"
**Solution**: Check valid values:
- Income: low, medium, high, very_high
- Employment: employed, self_employed, unemployed, retired
- Education: high_school, bachelor, master, phd
- Marital: single, married, divorced, widowed

### Issue: Python script "command not found"
**Solution**: Activate virtual environment first:
```bash
.venv\Scripts\Activate.ps1
```

### Issue: Feature validation failed
**Solution**: Verify numeric ranges:
- Health Score: 1-10
- Exercise: 0-7 days/week
- Years Insured: 0-50
- Age: 1-120
- BMI: 1-80

---

## 📊 Expected Trends

These are general trends you should observe:

| Factor | Effect | Impact |
|--------|--------|--------|
| Age | Increases with age | Strong ↑↑↑ |
| BMI | Higher BMI = higher cost | Strong ↑↑↑ |
| Smoker | Significant increase | Very Strong ↑↑↑↑ |
| Exercise | More exercise = lower cost | Medium ↓ |
| Health Score | Better health = lower cost | Medium ↓ |
| Income (Very High) | Discount applied | Medium ↓ |
| Education | Higher education = discount | Mild ↓ |
| Years Insured | Longer history = slight increase | Very Mild ↑ |

---

## 🎯 Next Steps

1. **Train the model**: `python train_model.py`
2. **Start the app**: `mvn spring-boot:run`
3. **Test on web**: Open `http://localhost:8080`
4. **Fill the form**: Include new fields
5. **Review results**: Check profile summary and suggestions
6. **Iterate**: Try different combinations to understand feature impact

---

## 📞 Need Help?

Check these files for detailed information:
- [FEATURE_EXPANSION_SUMMARY.md](FEATURE_EXPANSION_SUMMARY.md) - Complete feature guide
- [FEATURES_MODIFIED.md](FEATURES_MODIFIED.md) - Files changed summary
- [ML_MODEL_README.md](ML_MODEL_README.md) - Original ML documentation

---

**Status**: ✅ Ready to test!  
**Last Updated**: February 22, 2026  
**Version**: 2.0 - Feature Expansion Complete
