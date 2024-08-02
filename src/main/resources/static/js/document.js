async function deleteDocument(documentId, userId){
    let origin = window.location.origin;
    await fetch(`${origin}/documents/myDocs/${userId}/${documentId}/deleteDoc`, {method: "DELETE"});

    window.location.replace(origin);
}