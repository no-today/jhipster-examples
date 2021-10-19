package io.github.notoday.bff.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import io.github.notoday.bff.service.UserService;
import org.dataloader.BatchLoader;
import org.dataloader.Try;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * BatchLoader 会将多次单次调用 转成 一次批处理, 并将结果投递到目标调用。
 *
 * @author no-today
 * @date 2021/10/14 4:30 PM
 */
@DgsDataLoader(name = "avatar")
public class UserAvatarDataLoader implements BatchLoader<String, Try<String>> {

    private final UserService userService;

    public UserAvatarDataLoader(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CompletionStage<List<Try<String>>> load(List<String> list) {
        return CompletableFuture.supplyAsync(() -> list.parallelStream().map(key -> Try.tryCall(() -> userService.getUserImage(key)))
            .collect(Collectors.toList()));
    }
}
