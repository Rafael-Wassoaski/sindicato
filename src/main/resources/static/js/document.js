async function deleteDocument(documentId){
    let origin = window.location.origin;
    await fetch(`${origin}/documents/myDocs/${documentId}/{documentId}/deleteDoc`);

    window.location.replace(origin);
}