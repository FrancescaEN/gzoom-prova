package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.ContentAndAttributes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ContentAndAttributesMapper {

    List<ContentAndAttributes> getValidMenu(@Param("keys") List<String> keys, @Param("userLoginId") String userLoginId);

    List<ContentAndAttributes> getFolderMenu();
}
