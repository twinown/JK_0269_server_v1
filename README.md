Егор, [15.05.2025 13:23]
garage_cols = ['GarageQual', 'GarageFinish', 'GarageCond', 'GarageType']
Bsmt_cols= ["BsmtCond","BsmtExposure","BsmtQual","BsmtFinType2","BsmtFinType1"]

Егор, [15.05.2025 13:26]
garage_and_bsmt=garage_cols+Bsmt_cols
for col in garage_and_bsmt:
    df[col]= df[col].fillna("None")
