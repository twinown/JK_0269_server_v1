standard_scaler_columns = ['RtpStateBitfield','AVProductStatesIdentifier', 'CountryIdentifier', 'CityIdentifier','OrganizationIdentifier','GeoNameIdentifier','LocaleEnglishNameIdentifier','OsBuild','OsSuite','IeVerIdentifier',
                           'Census_OEMNameIdentifier','Census_OEMModelIdentifier','Census_ProcessorModelIdentifier','Census_PrimaryDiskTotalCapacity','Census_SystemVolumeTotalCapacity','Census_TotalPhysicalRAM','Census_InternalPrimaryDisplayResolutionHorizontal',
                           'Census_InternalPrimaryDisplayResolutionVertical','Census_InternalBatteryNumberOfCharges','Census_OSBuildNumber','Census_OSBuildRevision','Census_OSInstallLanguageIdentifier','Census_OSUILocaleIdentifier','Census_InternalPrimaryDiagonalDisplaySizeInInches',
                           'Census_FirmwareManufacturerIdentifier','Census_FirmwareVersionIdentifier','Wdft_RegionIdentifier']
normalized =ColumnTransformer(
    [
        ('scaling_num_columns', StandardScaler(), standard_scaler_columns)
    ],
    verbose_feature_names_out = False,
    remainder = 'passthrough' 
)
X, y = data.drop('HasDetections', axis=1), data['HasDetections']
X_train, X_valid, y_train, y_valid = train_test_split(X, y, test_size=0.2, stratify=y, random_state=42)
proc_x_train = normalized.fit_transform(X_train)
proc_x_valid = normalized.transform(X_valid)
model = LogisticRegression(
    penalty='elasticnet',
    class_weight='balanced',
    random_state=42,
)
model.fit(proc_x_train,y_train)

