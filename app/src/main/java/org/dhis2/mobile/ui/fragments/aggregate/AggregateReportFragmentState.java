/*
 * Copyright (c) 2015, University of Oslo
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.dhis2.mobile.ui.fragments.aggregate;

import android.os.Parcel;
import android.os.Parcelable;

import org.dhis2.mobile.api.models.DateHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dhis2.mobile.sdk.utils.Preconditions.isNull;
import static org.dhis2.mobile.utils.TextUtils.isEmpty;

public class AggregateReportFragmentState implements Parcelable {
    public static final Creator<AggregateReportFragmentState> CREATOR
            = new Creator<AggregateReportFragmentState>() {

        public AggregateReportFragmentState createFromParcel(Parcel in) {
            return new AggregateReportFragmentState(in);
        }

        public AggregateReportFragmentState[] newArray(int size) {
            return new AggregateReportFragmentState[size];
        }
    };
    private static final String TAG = AggregateReportFragmentState.class.getName();
    private boolean syncInProcess;

    private String orgUnitLabel;
    private String orgUnitId;

    private String dataSetLabel;
    private String dataSetId;
    private String dataSetCategoryComboId;

    private String periodLabel;
    private String periodDate;

    // private CategoryOptionState[] categoryOptionStates;
    private Map<String, String> categoryOptions;

    public AggregateReportFragmentState() {
        categoryOptions = new HashMap<>();
    }

    public AggregateReportFragmentState(AggregateReportFragmentState state) {
        if (state != null) {
            setSyncInProcess(state.isSyncInProcess());
            setOrgUnit(state.getOrgUnitId(), state.getOrgUnitLabel());
            setDataSet(state.getDataSetId(), state.getDataSetLabel(), state.getDataSetCategoryComboId());
            setPeriod(state.getPeriod());
            setCategoryOptions(state.getCategoryOptions());
        }

        if (getCategoryOptions() == null) {
            setCategoryOptions(new HashMap<String, String>());
        }
    }

    private AggregateReportFragmentState(Parcel in) {
        syncInProcess = in.readInt() == 1;

        orgUnitLabel = in.readString();
        orgUnitId = in.readString();

        dataSetLabel = in.readString();
        dataSetId = in.readString();
        dataSetCategoryComboId = in.readString();

        periodLabel = in.readString();
        periodDate = in.readString();

        categoryOptions = new HashMap<>();

        List<String> categoryIds = new ArrayList<>();
        List<String> categoryOptionIds = new ArrayList<>();

        in.readStringList(categoryIds);
        in.readStringList(categoryOptionIds);

        if (!categoryIds.isEmpty()) {
            for (int i = 0; i < categoryIds.size(); i++) {
                String categoryId = categoryIds.get(i);
                String categoryOptionId = categoryOptionIds.get(i);
                categoryOptions.put(categoryId, categoryOptionId);
            }
        }
    }

    @Override
    public int describeContents() {
        return TAG.length();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(syncInProcess ? 1 : 0);

        parcel.writeString(orgUnitLabel);
        parcel.writeString(orgUnitId);

        parcel.writeString(dataSetLabel);
        parcel.writeString(dataSetId);
        parcel.writeString(dataSetCategoryComboId);

        parcel.writeString(periodLabel);
        parcel.writeString(periodDate);

        List<String> categoryIds = new ArrayList<>();
        List<String> categoryOptionIds = new ArrayList<>();

        if (categoryOptions != null) {
            for (String categoryId : categoryOptions.keySet()) {
                categoryIds.add(categoryId);
                categoryOptionIds.add(categoryOptions.get(categoryId));
            }
        }

        parcel.writeStringList(categoryIds);
        parcel.writeStringList(categoryOptionIds);
    }

    public boolean isSyncInProcess() {
        return syncInProcess;
    }

    public void setSyncInProcess(boolean syncInProcess) {
        this.syncInProcess = syncInProcess;
    }

    public void setOrgUnit(String orgUnitId, String orgUnitLabel) {
        this.orgUnitId = orgUnitId;
        this.orgUnitLabel = orgUnitLabel;
    }

    public void resetOrgUnit() {
        orgUnitId = null;
        orgUnitLabel = null;
    }

    public boolean isOrgUnitEmpty() {
        return (orgUnitId == null || orgUnitLabel == null);
    }

    public String getOrgUnitLabel() {
        return orgUnitLabel;
    }

    public String getOrgUnitId() {
        return orgUnitId;
    }

    public void setDataSet(String dataSetId, String dataSetLabel, String categoryComboId) {
        this.dataSetId = dataSetId;
        this.dataSetLabel = dataSetLabel;
        this.dataSetCategoryComboId = categoryComboId;
    }

    public void resetDataSet() {
        dataSetId = null;
        dataSetLabel = null;
        dataSetCategoryComboId = null;
    }

    public boolean isDataSetEmpty() {
        return (dataSetId == null || dataSetLabel == null || dataSetCategoryComboId == null);
    }

    public String getDataSetLabel() {
        return dataSetLabel;
    }

    public String getDataSetId() {
        return dataSetId;
    }

    public String getDataSetCategoryComboId() {
        return dataSetCategoryComboId;
    }

    public DateHolder getPeriod() {
        return new DateHolder(periodDate, periodLabel);
    }

    public void setPeriod(DateHolder dateHolder) {
        if (dateHolder != null) {
            periodLabel = dateHolder.getLabel();
            periodDate = dateHolder.getDate();
        }
    }

    public void resetPeriod() {
        periodLabel = null;
        periodDate = null;
    }

    public boolean isPeriodEmpty() {
        return (periodLabel == null || periodDate == null);
    }

    private void setCategoryOptions(Map<String, String> categoryOptions) {
        this.categoryOptions = categoryOptions;
    }

    private Map<String, String> getCategoryOptions() {
        return categoryOptions;
    }

    public void setCategoryOption(String categoryId, String categoryOptionId) {
        isNull(categoryId, "Category ID object must not be null");
        isNull(categoryOptionId, "CategoryOption ID object must not be null");
        categoryOptions.put(categoryId, categoryOptionId);
    }

    public void resetCategoryOptions() {
        categoryOptions.clear();
    }

    public boolean areCategoryOptionsEmpty() {
        return categoryOptions.isEmpty();
    }

    public boolean isCategoryOptionSelected(String categoryId) {
        isNull(categoryId, "Category ID object must not be null");
        return (categoryOptions.containsKey(categoryId) &&
                !isEmpty(categoryOptions.get(categoryId)));
    }

    public static class CategoryOptionState implements Parcelable {
        public static final Creator<CategoryOptionState> CREATOR
                = new Creator<CategoryOptionState>() {

            public CategoryOptionState createFromParcel(Parcel in) {
                return new CategoryOptionState(in);
            }

            public CategoryOptionState[] newArray(int size) {
                return new CategoryOptionState[size];
            }
        };

        private static final String TAG = CategoryOptionState.class.getSimpleName();
        private String categoryId;
        private String categoryOptionId;

        private CategoryOptionState(Parcel in) {
            categoryId = in.readString();
            categoryOptionId = in.readString();
        }

        @Override
        public int describeContents() {
            return TAG.length();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(categoryId);
            dest.writeString(categoryOptionId);
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryOptionId() {
            return categoryOptionId;
        }

        public void setCategoryOptionId(String categoryOptionId) {
            this.categoryOptionId = categoryOptionId;
        }
    }
}