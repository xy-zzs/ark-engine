package io.ark.engine.core.result;

import java.util.List;

/**
 * @Description: 分页响应体
 *
 * <p>统一分页数据结构，与 MyBatis-Plus 的 {@code IPage} 解耦， application 层返回此对象，infrastructure 层负责从 {@code
 * IPage} 转换， 保证 MP 不泄漏到 application 层以上。
 *
 * @param <T> 列表元素类型 @Author: Noah Zhou
 */
public class PageResult<T> {

  /** 当前页数据 */
  private final List<T> records;

  /** 总记录数 */
  private final long total;

  /** 当前页码（从 1 开始） */
  private final long current;

  /** 每页大小 */
  private final long size;

  public PageResult(List<T> records, long total, long current, long size) {
    this.records = records;
    this.total = total;
    this.current = current;
    this.size = size;
  }

  /** 从任意分页数据快速构建，infrastructure 层使用 */
  public static <T> PageResult<T> of(List<T> records, long total, long current, long size) {
    return new PageResult<>(records, total, current, size);
  }

  public List<T> getRecords() {
    return records;
  }

  public long getTotal() {
    return total;
  }

  public long getCurrent() {
    return current;
  }

  public long getSize() {
    return size;
  }

  /** 总页数 */
  public long getPages() {
    if (size == 0) {
      return 0;
    }
    return (total + size - 1) / size;
  }
}
