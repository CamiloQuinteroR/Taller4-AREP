async function task(tarea) {
  try {
    //Realizamos la solicitud al backend con el nombre en la URI 
    const solicitud = await fetch(`/app/task?name=${encodeURIComponent(tarea)}`);
    // Convertimos la respuesta a JSON
    const text = await solicitud.json();
    const elemntoTarea = document.createElement("li");
    elemntoTarea.textContent = text.mensaje; 
    document.getElementById("lista").appendChild(elemntoTarea);
  } catch (err) {
    console.error("Error consultando el servicio:", err);
  }
}

document.getElementById("formulario").addEventListener("submit", (event) => {
  event.preventDefault();
  const nombre = document.getElementById("input").value.trim();
  if (nombre) {
    task(nombre);
    document.getElementById("input").value = ""; 
  }
});
