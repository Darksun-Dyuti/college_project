# Insurance Cost Prediction - Feature Expansion Summary

## Overview
The insurance cost prediction system has been significantly expanded with **7 new features** that provide a more comprehensive assessment of insurance costs based on lifestyle, education, employment, and health factors.

---

## 🎯 New Features Added

### 1. **Income Level**
- **Type**: Categorical
- **Values**: `low`, `medium`, `high`, `very_high`
- **Impact**: Lower income slightly increases premium risk; higher income receives discounts
- **Default**: `medium`

### 2. **Employment Status**
- **Type**: Categorical  
- **Values**: `employed`, `self_employed`, `unemployed`, `retired`
- **Impact**: Employment stability affects risk assessment (unemployed = higher risk)
- **Default**: `employed`

### 3. **Health Score**
- **Type**: Numeric (1-10 scale)
- **Range**: 1 (Poor) to 10 (Excellent)
- **Impact**: Lower health scores indicate pre-existing conditions or health concerns (increases cost)
- **Default**: 7

### 4. **Exercise Frequency**
- **Type**: Numeric (days per week)
- **Range**: 0 to 7 days per week
- **Impact**: Regular exercise reduces health risks and insurance costs
- **Default**: 3 days/week

### 5. **Education Level**
- **Type**: Categorical
- **Values**: `high_school`, `bachelor`, `master`, `phd`
- **Impact**: Higher education correlates with health awareness and better preventive care
- **Default**: `bachelor`

### 6. **Marital Status**
- **Type**: Categorical
- **Values**: `single`, `married`, `divorced`, `widowed`
- **Impact**: Marital status can influence health behaviors and risk assessment
- **Default**: `single`

### 7. **Years Insured**
- **Type**: Numeric (0-50 years)
- **Range**: 0 to 50 years
- **Impact**: Longer insurance history indicates stable, low-risk customers
- **Default**: 0

---

## 📁 Files Modified

### 1. **train_model.py** (Model Training)
**Changes**:
- Added 7 new categorical and numeric features to synthetic data generation
- Extended cost calculation formula with new feature weights
- Created LabelEncoders for: `income`, `employment`, `education`, `marital_status`
- Updated feature list from 6 to 13 features
- Enhanced model configuration JSON with all encoders and feature names

**Key Updates**:
```python
# New features added to synthetic data
'income_level', 'employment_status', 'health_score', 'exercise_frequency',
'education_level', 'marital_status', 'years_insured'

# Enhanced cost formula now includes:
- Income-based premiums/discounts
- Employment status risk factors
- Health score impact (inverse relationship)
- Exercise frequency benefits
- Education level adjustments
- Years insured credits
```

### 2. **predict_model.py** (Prediction Script)
**Changes**:
- Updated documentation with all 13 parameters
- Modified argument parsing to support both `key=value` format and legacy positional format
- Added handling for all new categorical encoders
- Enhanced feature vector building with new features
- Backward compatible with legacy code

**Usage Examples**:
```bash
# Using key=value format (recommended)
python predict_model.py age=45 gender=female bmi=28.5 kids=2 smoker=no \
  location=northeast income=medium employment=employed health_score=7 \
  exercise_frequency=4 education=bachelor marital_status=married years_insured=10

# Using legacy positional format (still supported)
python predict_model.py 45 female 28.5 2 no northeast
```

### 3. **InsuranceRequest.java** (Backend DTO)
**Changes**:
- Added 7 new private fields with getter/setter methods
- Fields: `income`, `employment`, `healthScore`, `exerciseFrequency`, `education`, `maritalStatus`, `yearsInsured`
- Maintains backward compatibility with existing fields

### 4. **PredictController.java** (API Endpoint Validation)
**Changes**:
- Enhanced validation for all new fields
- Added enum-like validation for categorical fields
- Implemented range validation: health_score (1-10), exercise_frequency (0-7), years_insured (0-50)
- Smart defaults for optional new fields
- Enhanced response with detailed profile information and personalized suggestions

**Validation Examples**:
```java
// Health Score: Must be 1-10
if (healthScore < 1 || healthScore > 10) {
    return error response
}

// Exercise Frequency: 0-7 days per week
if (exerciseFrequency < 0 || exerciseFrequency > 7) {
    return error response
}
```

### 5. **MLPredictionService.java** (Service Layer)
**Changes**:
- Updated `predictInsuranceCost()` method signature with 7 new parameters
- Changed from legacy positional arguments to `key=value` format for clarity
- Enhanced logging to include all 13 parameters
- Process builder now passes structured key=value arguments to Python script

**Method Signature**:
```java
public Map<String, Object> predictInsuranceCost(
    int age, String gender, double bmi, int kids, boolean smoker, String location,
    String income, String employment, int healthScore, int exerciseFrequency,
    String education, String maritalStatus, int yearsInsured, String modelName)
```

### 6. **index.html** (Frontend Form)
**Changes**:
- Added 7 new form sections for additional health & lifestyle information
- Visual separator (hr) between basic and new information sections
- New input fields with appropriate types and defaults:
  - Select dropdowns for: income, employment, education, maritalStatus
  - Number inputs for: healthScore, exerciseFrequency, yearsInsured

**New Form Fields**:
```html
<select id="income"> - Income Level dropdown
<select id="employment"> - Employment Status dropdown
<input type="number" id="healthScore"> - Health Score (1-10)
<input type="number" id="exerciseFrequency"> - Exercise Frequency (0-7)
<select id="education"> - Education Level dropdown
<select id="maritalStatus"> - Marital Status dropdown
<input type="number" id="yearsInsured"> - Years Insured (0-50)
```

**JavaScript Updates**:
- Enhanced payload object to include all 7 new fields
- Values are properly typed (strings for selects, integers for numbers)
- New fields added to fetch request body

---

## 🔄 Data Flow

### Request Flow:
```
Frontend (index.html) 
  ↓ (JSON with 13 parameters)
PredictController (validates all fields)
  ↓ (calls with 13 parameters)
MLPredictionService (builds command)
  ↓ (key=value format arguments)
predict_model.py (parses arguments)
  ↓ (builds feature vector)
ML Models (random_forest, decision_tree, linear_regression)
  ↓ (prediction output)
Response (cost estimate + suggestions)
  ↓ (JSON response)
Frontend (displays result)
```

---

## 📊 Model Feature Set (13 Total)

### Original Features (6):
1. age
2. gender (encoded)
3. bmi
4. kids
5. smoker (encoded as integer)
6. location (encoded)

### New Features (7):
7. income_level (encoded)
8. employment_status (encoded)
9. health_score (numeric)
10. exercise_frequency (numeric)
11. education_level (encoded)
12. marital_status (encoded)
13. years_insured (numeric)

---

## 🚀 How to Use

### 1. Train the Model with New Features:
```bash
python train_model.py
```
This will:
- Generate synthetic data with all 13 features
- Train three models with expanded feature set
- Save all encoders for the new categorical features
- Create updated model_config.json with all features

### 2. Make Predictions via Web Form:
1. Navigate to the prediction form (index.html)
2. Fill in all fields:
   - **Basic Info**: Name, Age, Gender, BMI, Number of Kids, Smoker Status
   - **Location & Model**: Region, Model Selection
   - **New Fields**: Income, Employment, Health Score, Exercise, Education, Marital Status, Years Insured
3. Click "Get Estimate"
4. View detailed results with personalized suggestions

### 3. Test via Python Script (Direct):
```bash
python src/main/resources/predict_model.py \
  age=45 gender=female bmi=28.5 kids=2 smoker=no \
  location=northeast income=high employment=employed \
  health_score=8 exercise_frequency=5 education=master \
  marital_status=married years_insured=10 --model random_forest
```

---

## 💡 Prediction Examples

### Low-Cost Profile:
```
Age: 30, Female, BMI: 22, No kids, Non-smoker
Northeast, Very High Income, Employed, Health Score: 9
Exercises 6 days/week, Master's Degree, Married, 5 years insured
❌ Expected Cost: ~$3,500-4,500/year
```

### High-Cost Profile:
```
Age: 60, Male, BMI: 35, 3 kids, Smoker
Southwest, Low Income, Unemployed, Health Score: 4
Exercises 1 day/week, High School, Single, 20 years insured
⚠️ Expected Cost: ~$12,000-15,000/year
```

---

## 🔐 Validation Rules

| Field | Type | Min | Max | Valid Values | Default |
|-------|------|-----|-----|--------------|---------|
| Age | Integer | 1 | 120 | - | - |
| BMI | Float | 1 | 80 | - | - |
| Kids | Integer | 0 | 10 | - | 0 |
| Health Score | Integer | 1 | 10 | - | 7 |
| Exercise | Integer | 0 | 7 | - | 3 |
| Years Insured | Integer | 0 | 50 | - | 0 |
| Gender | String | - | - | male, female, other | - |
| Smoker | Boolean | - | - | true, false | false |
| Location | String | - | - | northeast, southeast, southwest, northwest | - |
| Income | String | - | - | low, medium, high, very_high | medium |
| Employment | String | - | - | employed, self_employed, unemployed, retired | employed |
| Education | String | - | - | high_school, bachelor, master, phd | bachelor |
| Marital | String | - | - | single, married, divorced, widowed | single |

---

## 📈 Impact on Model Accuracy

The new features enhance the model's ability to:
- ✅ Assess lifestyle health factors (exercise, health score)
- ✅ Account for educational attainment and awareness
- ✅ Consider employment stability and income level
- ✅ Factor in relationship status and family structure
- ✅ Recognize customer loyalty (years insured)

---

## ⚠️ Backward Compatibility

- ✅ Legacy API requests still work (with smart defaults for new fields)
- ✅ Python script supports both positional and key=value argument formats
- ✅ Frontend gracefully handles missing new fields with sensible defaults
- ✅ Existing trained models require retraining to use all features

---

## 🔧 Next Steps

1. **Train the model** with new features:
   ```bash
   cd c:\Users\ASIF EBRAHIM\Downloads\demo
   python train_model.py
   ```

2. **Build and run** the application:
   ```bash
   mvn clean spring-boot:run
   ```

3. **Access the form** at:
   ```
   http://localhost:8080
   ```

4. **Test predictions** with the new comprehensive feature set!

---

## 📝 Notes

- All new features have intelligent defaults for backward compatibility
- The model automatically encodes categorical variables
- Personalized suggestions now include education, health, and lifestyle recommendations
- The response includes a complete profile summary for transparency
- All validation happens on both frontend and backend

---

**Last Updated**: February 22, 2026
**Version**: 2.0 - Enhanced Feature Set
