nan_counts = df.isna().sum()
nan_counts = nan_counts[nan_counts > 0]  # только с пропусками
print(nan_counts.sort_values(ascending=False))  # сортировка по убыванию
