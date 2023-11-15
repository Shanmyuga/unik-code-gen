import fetch from 'isomorphic-fetch';

export function ${actionMethodName} (args) {
    return fetch (
      '${url}', {
          headers: {
              'Accept': '${contentType}',
              'Content-Type': '${contentType}'
          },
          method: '${method}',
          credentials: 'include'
      }
    )
    .then((response) => {
        const json = response.json();
        console.log(json);
        return json;
    })
    .then((json) => json);
}