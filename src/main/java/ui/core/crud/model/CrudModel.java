package ui.core.crud.model;

import com.jgoodies.validation.ValidationResultModel;

public interface CrudModel<T> {

    T getId();

    ValidationResultModel getValidationResultModel();

}