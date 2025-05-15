last_col=['MSZoning','Utilities','Functional','Exterior1st','Exterior2nd','Electrical','SaleType','KitchenQual']
for col in last_col:
    df[col]= df[col].fillna(df[col].mode()[0])
