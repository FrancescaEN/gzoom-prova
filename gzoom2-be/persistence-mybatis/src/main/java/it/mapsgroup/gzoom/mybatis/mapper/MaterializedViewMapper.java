package it.mapsgroup.gzoom.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MaterializedViewMapper {

    void refreshMaterialView();

}