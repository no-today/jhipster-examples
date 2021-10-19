package io.github.notoday.bff.fatchers;

import com.netflix.graphql.dgs.*;
import io.github.notoday.bff.service.dto.PageDTO;
import io.github.notoday.bff.service.dto.PublicUsersDTO;
import io.github.notoday.bff.loaders.UserAvatarDataLoader;
import io.github.notoday.commons.core.domain.IPage;
import io.github.notoday.uaa.client.UaaClient;
import io.github.notoday.uaa.service.dto.UserDTO;
import org.dataloader.DataLoader;
import org.springframework.data.domain.PageRequest;

import java.util.concurrent.CompletableFuture;

/**
 * @author no-today
 * @date 2021/10/08 4:07 PM
 */
@DgsComponent
public class UserInfoDataFetcher {

    private final UaaClient uaaClient;

    public UserInfoDataFetcher(UaaClient uaaClient) {
        this.uaaClient = uaaClient;
    }

    @DgsQuery
    public PublicUsersDTO publicUsers(@InputArgument Integer page, @InputArgument Integer size) {
        IPage<UserDTO> result = uaaClient.getAllPublicUsers(PageRequest.of(page, size)).getBody();
        return new PublicUsersDTO()
            .setList(result.getList())
            .setPage(PageDTO.of(result));
    }

    @DgsData(parentType = "User", field = "avatar")
    public CompletableFuture<String> avatar(DgsDataFetchingEnvironment dfe) {
        DataLoader<String, String> dataLoader = dfe.getDataLoader(UserAvatarDataLoader.class);
        return dataLoader.load(((UserDTO) dfe.getSource()).getId());
    }

    @DgsMutation
    public UserDTO updateUser(@InputArgument UserDTO user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        return dto;
    }
}
