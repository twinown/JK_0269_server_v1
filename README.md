col_fil_na=['MasVnrArea','GarageYrBlt','BsmtHalfBath','GarageArea', 'GarageCars', 'BsmtFullBath', 'TotalBsmtSF', 'BsmtUnfSF', 'BsmtFinSF1', 'BsmtFinSF2']
for col in col_fil_na:
    df[col]= df[col].fillna(0)
