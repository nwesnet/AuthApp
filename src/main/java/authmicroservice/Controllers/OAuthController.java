package authmicroservice.Controllers;

import authmicroservice.Utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class OAuthController {
    @PostMapping("/token")
    public Map<String, String> exchangeToken(@RequestParam String code) {
        String username = CodeStore.getUsernameByCode(code);

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid code");
        }

        String jwt = JwtUtil.generateToken(username);

        CodeStore.removeCode(code);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        return response;
    }
}
