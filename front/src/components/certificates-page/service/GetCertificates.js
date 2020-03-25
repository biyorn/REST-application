import Axios from 'axios'   

export function getCertificates(params) {
    const pageSize = params.pageSize;
    const pageNum = params.pageNum ? params.pageNum : 1;
    const partName = params.partName;
    const tagName = params.tagName;
    const sort = params.sort;
    let tags = ''
    if(Array.isArray(tagName)) {
        tags = tags + params.tagName.map(tag => tag.toString())
    } else {
        tags = tagName;
    }
    return (
        Axios.get('http://localhost:8080/certificates', {
            params: {
                pageNum: pageNum,
                pageSize: pageSize,
                partName: partName,
                tagName: tags,
                sort: sort
            }
        })
            .then(response => {
                return (
                    response
                )
            })
    )
}