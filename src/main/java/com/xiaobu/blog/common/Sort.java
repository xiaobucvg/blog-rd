package com.xiaobu.blog.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zh  --2020/3/18 20:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sort{
    private String name;

    private String rule;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sort sort = (Sort) o;
        return name != null ? name.equals(sort.name) : sort.name == null;
    }
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
