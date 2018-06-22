package com.ksatstuttgart.usoc.gui.setup.configuration.entity.data;

import com.ksatstuttgart.usoc.gui.setup.configuration.entity.UIEntity;

import java.util.Objects;

public class VarDTO implements UIEntity {

    private String varName;

    private String dataType;

    public VarDTO(String varName, String dataType) {
        this.varName = varName;
        this.dataType = dataType;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VarDTO varDTO = (VarDTO) o;
        return Objects.equals(varName, varDTO.varName) &&
                Objects.equals(dataType, varDTO.dataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName, dataType);
    }

    @Override
    public String toString() {
        return varName;
    }
}
