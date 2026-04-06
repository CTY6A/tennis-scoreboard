package com.stubedavd.service;

import java.util.UUID;

public interface CrudService<RequestDto, ResponseDto> {

    ResponseDto create(RequestDto requestDto);

    void delete(UUID uuid);
}
