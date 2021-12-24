package io.github.notoday.wallet.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author no-today
 * @date 2021/10/08 3:53 PM
 */
@FeignClient("wallet")
public interface WalletClient {
}
