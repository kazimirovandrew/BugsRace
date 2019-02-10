package com.basecamp.service;

import com.basecamp.wire.GetHandleProductIdsResponse;
import com.basecamp.wire.GetProductInfoResponse;
import com.basecamp.wire.GetRaceStatusResponse;

import java.util.List;

public interface ProductService {

    GetRaceStatusResponse homework();

    GetProductInfoResponse getProductInfo(String productId);

    GetHandleProductIdsResponse handleProducts(List<String> productIds);

    void stopProductExecutor();

}
