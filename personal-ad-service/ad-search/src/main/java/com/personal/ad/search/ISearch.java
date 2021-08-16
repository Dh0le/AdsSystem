package com.personal.ad.search;

import com.personal.ad.search.vo.SearchRequest;
import com.personal.ad.search.vo.SearchResponse;

public interface ISearch {
    SearchResponse fetchAds(SearchRequest request);
}
