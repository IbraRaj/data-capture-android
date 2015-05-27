package org.dhis2.mobile.sdk.persistence.models;

import com.raizlabs.android.dbflow.structure.BaseModel;

import static org.dhis2.mobile.sdk.utils.Preconditions.isNull;

/**
 * This class is intended to implement partial
 * functionality of ContentProviderOperation for DbFlow.
 */
public final class DbOperation {
    private final OperationType mOperationType;
    private final BaseModel mModel;

    public enum OperationType {
        INSERT, UPDATE, DELETE
    }

    private DbOperation(OperationType operationType, BaseModel model) {
        mOperationType = operationType;
        mModel = isNull(model, "BaseModel object must nto be null,");
    }

    public static <T extends BaseModel> DbOperation insert(T model) {
        return new DbOperation(OperationType.INSERT, model);
    }

    public static <T extends BaseModel> DbOperation update(T model) {
        return new DbOperation(OperationType.UPDATE, model);
    }

    public static <T extends BaseModel> DbOperation delete(T model) {
        return new DbOperation(OperationType.DELETE, model);
    }

    public BaseModel getModel() {
        return mModel;
    }

    public OperationType getOperationType() {
        return mOperationType;
    }
}