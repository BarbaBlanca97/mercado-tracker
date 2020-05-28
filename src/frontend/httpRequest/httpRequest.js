const httpRequest = async function (url, method, data) {
    let requestInfo = {
        method,
        credentials: 'include',
        headers: {}
    };
    
    if (data) {
        requestInfo.headers = { 'Content-Type': 'application/json' };
        requestInfo.body = JSON.stringify(data);
    }

    let accesToken = localStorage.getItem('authorization');
    if (accesToken) {
        requestInfo.headers['Authorization'] = accesToken;
    }
    
    const request = new Request(url, requestInfo);
    const response = await fetch(request);
    const responseData = await response.json();

    accesToken = response.headers.get('Authorization');
    if (accesToken) 
        localStorage.setItem('authorization', accesToken);
    
    if (response.ok) {
        return responseData;
    }
    else {
        console.error(responseData);
        throw responseData;
    }
}

export default httpRequest;