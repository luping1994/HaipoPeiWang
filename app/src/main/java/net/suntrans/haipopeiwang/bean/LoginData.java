package net.suntrans.haipopeiwang.bean;

/**
 * Created by Looney on 2018/8/16.
 * Des:
 */
public class LoginData {

    /**
     * token : {"token_type":"Bearer","expires_in":604800,"access_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjRkYjJmNWJkNjgyZTZkMDQ4ODcxODRhYzQ2NDNmZDhlMTY4ODI5MWUwOGRlZGIwMWE1YzY1MDVjYWYxYzI0MjBlNTQ3YzJiYmFjOTRmNTE5In0.eyJhdWQiOiIyIiwianRpIjoiNGRiMmY1YmQ2ODJlNmQwNDg4NzE4NGFjNDY0M2ZkOGUxNjg4MjkxZTA4ZGVkYjAxYTVjNjUwNWNhZjFjMjQyMGU1NDdjMmJiYWM5NGY1MTkiLCJpYXQiOjE1MzQ0MDYwNzIsIm5iZiI6MTUzNDQwNjA3MiwiZXhwIjoxNTM1MDEwODcyLCJzdWIiOiI0OSIsInNjb3BlcyI6W119.h1koPHZS5VwJ12Btvu6EGex4MvVBj0VbP7pPSfoP8aHJ6VJ2mQNo6MaYnaLUjjpsurvSYa6qrqf_uEAlJ8Wyv7lRRnf7dxe7iXV_QzDrdoQ9H_8CSG_bF7UlfiuMGTnsFu7GTsp1OKCgCLTbZ78gsiMoeeDYK87sieS6hz-sf0okmFpnFN2LkPZ3c46teRfe0CFljsXr-pib28b65NBsRsO0bJE6Gkao73iXJOMmD9n5jjOdQwKpSN6EWhoqbZFyvkuJqr5n3fZdg8AIAedjS8oah1RkMa0FEbHeXBEnU1nSi0FYX34jqs-qm5N8iL3jbiR3QFBsGltsHuzJfg2KahFM1sb0I0pHXE9NqsyTLJ2Xe9vNgt56L-3AFNQER9HKSE-d86shaA905KkXpeUKtUAq6UYfvFS_4elKOLKP0FRhamsjH_k94UCfvjGb19nXNQ1__-vRuUIBVYOwea5hh7a0KCTrdFNeOQscdglid3D8VPPRKnSLFscZH5WLIKDHpLUAiYDTNPnRmNtEm0nFV2DUsrFX4MH37WYm8yqLyTWJkJXp_h9LU0HnjFaFywVvd_x_XyUBnAeS_muKdgcpWmg5Tbl6qplUxJb5rCfV7TFwZ0KrpPHI0mXjbPo3J8SmYp4BYtyZ4Md-NvQrLJWbjOoAMmZDej_oy-zaKMOKjHI","refresh_token":"VKx35okGyDsRhd0xVsICwoOfjKYzUpyqhqOpSkxHES77CvRwrISqnkgHXZ7INN3VQyM1bl0tdhcvkuMU9c8+tzT9e5TTRYQJ8IYfQO1SLs+yBygbweML+zIP0tiqyH029Wa+LqHi2G7CiQRWj3JOMqhCe5DqvRLMhOx28kAV9uGDsP3uQajypzB9r2QwEZPh2JqU1yQ1pKPDepBnIKLvG+zw0r4j7yxbEcqDRdqxKC8yNBBf0C2HAhT8Usku2ioESP7EIwjdK4MG890uxVEeNdBuvoVIFQyjKzURpwITOOgEGEv0hk9oOw7pejo3aNReqg/bAosW7AVq2EF/U/pB9k0GmWdVkU5vSxJ5Ev02cknUfiTl5lavuPn8UmjVGeFENIz0vAJJR8wfF3O3GWJyIexvcAswfcVShwToLWf5mNudot4AufrWlAqR5PMVcVaMV/RY+7VmvxSXAGwffkg0cOsIkXXwq4ribNmBPOW7VUxX6eiZt0VEYi0WgTg+rtL/Nx5brYYXeFSHK4PKydM/9jezefpifzOPhy2ZrGZpj/YAksLj6mzV6MerJI+W/AttfEG0VQlx0H3cEO4GXyWayArKptX+H96VvRUVegBjxdA0uJmWLOOnFxxgKm2roZm3Pnp1Q7kmzBf6FWRn6wSHjESwreK1dqkCxQctqKFqbi8="}
     * user : {"id":49,"username":"15899694150","name":"卢平","phone":"15899694150","status":1,"salt":"7BTfat","weixin_id":null,"union_id":1,"role_id":1}
     */

    public TokenBean token;
    public UserBean user;

    public static class TokenBean {
        /**
         * token_type : Bearer
         * expires_in : 604800
         * access_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjRkYjJmNWJkNjgyZTZkMDQ4ODcxODRhYzQ2NDNmZDhlMTY4ODI5MWUwOGRlZGIwMWE1YzY1MDVjYWYxYzI0MjBlNTQ3YzJiYmFjOTRmNTE5In0.eyJhdWQiOiIyIiwianRpIjoiNGRiMmY1YmQ2ODJlNmQwNDg4NzE4NGFjNDY0M2ZkOGUxNjg4MjkxZTA4ZGVkYjAxYTVjNjUwNWNhZjFjMjQyMGU1NDdjMmJiYWM5NGY1MTkiLCJpYXQiOjE1MzQ0MDYwNzIsIm5iZiI6MTUzNDQwNjA3MiwiZXhwIjoxNTM1MDEwODcyLCJzdWIiOiI0OSIsInNjb3BlcyI6W119.h1koPHZS5VwJ12Btvu6EGex4MvVBj0VbP7pPSfoP8aHJ6VJ2mQNo6MaYnaLUjjpsurvSYa6qrqf_uEAlJ8Wyv7lRRnf7dxe7iXV_QzDrdoQ9H_8CSG_bF7UlfiuMGTnsFu7GTsp1OKCgCLTbZ78gsiMoeeDYK87sieS6hz-sf0okmFpnFN2LkPZ3c46teRfe0CFljsXr-pib28b65NBsRsO0bJE6Gkao73iXJOMmD9n5jjOdQwKpSN6EWhoqbZFyvkuJqr5n3fZdg8AIAedjS8oah1RkMa0FEbHeXBEnU1nSi0FYX34jqs-qm5N8iL3jbiR3QFBsGltsHuzJfg2KahFM1sb0I0pHXE9NqsyTLJ2Xe9vNgt56L-3AFNQER9HKSE-d86shaA905KkXpeUKtUAq6UYfvFS_4elKOLKP0FRhamsjH_k94UCfvjGb19nXNQ1__-vRuUIBVYOwea5hh7a0KCTrdFNeOQscdglid3D8VPPRKnSLFscZH5WLIKDHpLUAiYDTNPnRmNtEm0nFV2DUsrFX4MH37WYm8yqLyTWJkJXp_h9LU0HnjFaFywVvd_x_XyUBnAeS_muKdgcpWmg5Tbl6qplUxJb5rCfV7TFwZ0KrpPHI0mXjbPo3J8SmYp4BYtyZ4Md-NvQrLJWbjOoAMmZDej_oy-zaKMOKjHI
         * refresh_token : VKx35okGyDsRhd0xVsICwoOfjKYzUpyqhqOpSkxHES77CvRwrISqnkgHXZ7INN3VQyM1bl0tdhcvkuMU9c8+tzT9e5TTRYQJ8IYfQO1SLs+yBygbweML+zIP0tiqyH029Wa+LqHi2G7CiQRWj3JOMqhCe5DqvRLMhOx28kAV9uGDsP3uQajypzB9r2QwEZPh2JqU1yQ1pKPDepBnIKLvG+zw0r4j7yxbEcqDRdqxKC8yNBBf0C2HAhT8Usku2ioESP7EIwjdK4MG890uxVEeNdBuvoVIFQyjKzURpwITOOgEGEv0hk9oOw7pejo3aNReqg/bAosW7AVq2EF/U/pB9k0GmWdVkU5vSxJ5Ev02cknUfiTl5lavuPn8UmjVGeFENIz0vAJJR8wfF3O3GWJyIexvcAswfcVShwToLWf5mNudot4AufrWlAqR5PMVcVaMV/RY+7VmvxSXAGwffkg0cOsIkXXwq4ribNmBPOW7VUxX6eiZt0VEYi0WgTg+rtL/Nx5brYYXeFSHK4PKydM/9jezefpifzOPhy2ZrGZpj/YAksLj6mzV6MerJI+W/AttfEG0VQlx0H3cEO4GXyWayArKptX+H96VvRUVegBjxdA0uJmWLOOnFxxgKm2roZm3Pnp1Q7kmzBf6FWRn6wSHjESwreK1dqkCxQctqKFqbi8=
         */

        public String token_type;
        public long expires_in;
        public String access_token;
        public String refresh_token;
    }

    public static class UserBean {
        /**
         * id : 49
         * username : 15899694150
         * name : 卢平
         * phone : 15899694150
         * status : 1
         * salt : 7BTfat
         * weixin_id : null
         * union_id : 1
         * role_id : 1
         */

        public int id;
        public String username;
        public String name;
        public String phone;
        public int status;
        public String salt;
        public String weixin_id;
        public int union_id;
        public int role_id;
    }
}
