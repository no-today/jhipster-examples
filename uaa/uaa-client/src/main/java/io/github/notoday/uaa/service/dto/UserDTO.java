package io.github.notoday.uaa.service.dto;

public class UserDTO {

    private String id;

    private String login;

    private String avatar;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                "}";
    }
}
