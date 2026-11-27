window.NiceAPI = (() => {
  async function request(path, method = 'GET', data) {
    let response;
    try { response = await fetch(path, {method, cache:'no-store', headers:{Accept:'application/json', ...(data===undefined?{}:{'Content-Type':'application/json'})}, ...(data===undefined?{}:{body:JSON.stringify(data)})}); }
    catch(e) { throw new Error('Não foi possível conectar à API Java. Confira se o Spring Boot está em execução.'); }
    if(response.status===204) return null;
    const body = await response.json().catch(()=>null);
    if(!response.ok) throw new Error(body?.mensagem || body?.message || `Erro HTTP ${response.status}`);
    if(body===null) throw new Error('Resposta inválida da API.');
    return body;
  }
  async function snapshot(){const [clientes,produtos,pedidos]=await Promise.all(['clientes','produtos','pedidos'].map(k=>request('/'+k)));if(![clientes,produtos,pedidos].every(Array.isArray))throw new Error('Resposta inesperada da API.');return {clientes,produtos,pedidos};}
  const list=k=>request('/'+k), get=(k,id)=>request('/'+k+'/'+id), create=(k,data)=>request('/'+k,'POST',data), update=(k,id,data)=>request('/'+k+'/'+id,'PUT',data), remove=(k,id)=>request('/'+k+'/'+id,'DELETE');
  return {snapshot,list,get,create,update,remove};
})();
