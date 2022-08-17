package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE url = #{url}")
    Credential getCredential(String url);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> findAll(int userId);

    @Insert("INSERT INTO CREDENTIALS (url, username,key, password, userid) " +
            "VALUES(#{url}, #{userName},#{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int save(Credential note);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{id}")
    Credential findById(int id);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{id}")
    void delete(int id);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{userName}, password = #{password} WHERE credentialid = #{credentialId}")
    void update(Credential credential);
}
