package colfume.domain.perfume.service;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class PerfumeNotFoundException extends ColfumeException {

    public PerfumeNotFoundException() {
        super(ErrorCode.PERFUME_NOT_FOUND);
    }
}