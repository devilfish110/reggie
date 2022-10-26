function codeApi(data) {
    return $axios({
        'url': '/user/sendemail',
        'method': 'post',
        data
    })
}

function loginApi(data) {
    return $axios({
        'url': '/user/login',
        'method': 'post',
        data
    })
}

function loginoutApi() {
    return $axios({
        'url': '/user/loginout',
        'method': 'post',
    })
}

  