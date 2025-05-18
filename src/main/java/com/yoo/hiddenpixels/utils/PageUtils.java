package com.yoo.hiddenpixels.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;


// list -> page 로 변환
public class PageUtils {
    public static <T> Function<List<T>, Page<T>> toPage(Pageable pageable) {
        return list -> {
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), list.size());
            List<T> subList = list.subList(start, end);
            return new PageImpl<>(subList, pageable, list.size());
        };
    }
}