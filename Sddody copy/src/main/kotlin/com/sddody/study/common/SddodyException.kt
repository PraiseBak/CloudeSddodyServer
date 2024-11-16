package com.sddody.study.common

import com.sddody.study.helper.SddodyExceptionError


class SddodyException(val sddodyExceptionError: SddodyExceptionError) : RuntimeException(){
}