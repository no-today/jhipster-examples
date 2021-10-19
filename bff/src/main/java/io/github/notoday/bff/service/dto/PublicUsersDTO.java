package io.github.notoday.bff.service.dto;

import io.github.notoday.uaa.service.dto.UserDTO;

import java.util.List;

/**
 * @author no-today
 * @date 2021/10/11 5:38 PM
 */
public class PublicUsersDTO {

    private List<UserDTO> list;
    private PageDTO page;

    public List<UserDTO> getList() {
        return list;
    }

    public PublicUsersDTO setList(List<UserDTO> list) {
        this.list = list;
        return this;
    }

    public PageDTO getPage() {
        return page;
    }

    public PublicUsersDTO setPage(PageDTO page) {
        this.page = page;
        return this;
    }
}
