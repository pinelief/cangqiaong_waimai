package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);
    /**
     * 分页查询菜品
     * @param dishDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
    /**
     * 删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据菜品id查询菜品和对应的口味
     * @param id
     * @return
     */
    DishVO getByIdWithFlavors(Long id);

    /**
     * 更新菜品和对应的口味
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);
}
