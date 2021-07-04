package com.personal.ad.service;

import com.personal.ad.exception.AdException;
import com.personal.ad.vo.CreativeRequest;
import com.personal.ad.vo.CreativeResponse;

public interface ICreativeService {
    CreativeResponse creativeCreative(CreativeRequest request) throws AdException;
}
