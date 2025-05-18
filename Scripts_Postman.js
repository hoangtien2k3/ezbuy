const baseUrl = pm.environment.get("API_PREFIX");

const postRequest = {
    url: baseUrl + "/v1/auth/login",
    method: 'POST',
    header: {
        'Content-Type': 'application/json',
    },
    body: {
        mode: 'raw',
        raw: JSON.stringify({
            "username": "hoangtien2k3qx1@gmail.com",
            "password": "Tienha@!@#1",
            "clientId": "ezbuy-client"
        })
    }
};

pm.sendRequest(postRequest, (error, response) => {
    if (error) {
        console.error("Request error:", error);
    } else {
        const jsonData = response.json();

        if (response.code === 200 && jsonData.data) {
            const accessToken = jsonData.data.access_token;
            const refreshToken = jsonData.data.refresh_token;

            pm.environment.set("ACCESS_TOKEN", accessToken);
            pm.environment.set("REFRESH_TOKEN", refreshToken);

            console.log("Access Token:", accessToken);
            console.log("Refresh Token:", refreshToken);
        } else {
            console.error("Login failed or unexpected response:", response);
        }
    }
});
