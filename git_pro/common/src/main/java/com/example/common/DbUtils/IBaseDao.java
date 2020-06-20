package com.example.common.DbUtils;

import java.util.List;

public interface IBaseDao<T> {
    void release();
    void insert(T obj);
    void delete(T obj);
    void update(T obj);

    /**
     *
     * @param where 要匹配的cls 直接传入null或者new T() 是查询所有
     *              按条件查询:条件约束在T中的set方法 哪个字段设置了set就查询哪个字段 equal查询(返回完全匹配目标)
     * @return 数据集合
     */
    List<T> query(T where);
}
