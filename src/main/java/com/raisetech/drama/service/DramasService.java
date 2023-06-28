package com.raisetech.drama.service;

import com.raisetech.drama.dto.DramaDto;
import com.raisetech.drama.entity.Drama;


import java.util.List;

public interface DramasService {
    List<Drama> getDramas(String priority) throws Exception;

    int save(DramaDto dramaDto);
}
