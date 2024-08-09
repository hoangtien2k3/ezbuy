const baseUrl = pm.environment.get("API_PREFIX");

const postRequest = {
    url: baseUrl + "/login",
    method: 'POST',
    header: {
        'Content-Type': 'application/json',
    },
    body: {
        mode: 'raw',
        raw: JSON.stringify({
            "username" : "hoangtien2k3dev25@gmail.com",
            "password" : "tienha@!@#",
            "clientId" : "ezbuy-client"
        })
    }
};

pm.sendRequest(postRequest, (error, response) => {
    if (error) {
        console.error(error);
    } else {
        if (response.code === 200) {
            const access_token = JSON.parse(response.text()).data.access_token;
            const refresh_token = JSON.parse(response.text()).data.refresh_token;
            pm.environment.set("ACCESS_TOKEN", access_token);
            pm.environment.set("REFRESH_TOKEN", refresh_token);
            console.log(access_token);
            console.log(refresh_token);
        } else {
            console.error("Unexpected response code:", response.code);
        }
    }
});