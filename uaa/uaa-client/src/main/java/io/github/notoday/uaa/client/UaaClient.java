package io.github.notoday.uaa.client;

import io.github.notoday.commons.core.domain.IPage;
import io.github.notoday.uaa.service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author no-today
 * @date 2021/10/08 3:53 PM
 */
@FeignClient("uaa")
public interface UaaClient {

    @GetMapping("/api/services/users")
    ResponseEntity<IPage<UserDTO>> getAllPublicUsers(Pageable pageable);
}
