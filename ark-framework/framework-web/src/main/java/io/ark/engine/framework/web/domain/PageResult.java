package io.ark.engine.framework.web.domain;

import java.util.List;

/**
 * @Description: 分页结果
 * @Author: Noah Zhou
 */
public record PageResult<T>(List<T> records,
                            long total,
                            int page,
                            int size
) {
}
