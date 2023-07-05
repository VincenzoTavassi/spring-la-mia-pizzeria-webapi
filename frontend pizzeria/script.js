const contentElement = document.getElementById("content");

const getPizzas = async () => {
  try {
    const response = await axios.get("http://localhost:8080/api/v1/pizza");
    console.log(response.data.content);
    return response.data.content;
  } catch (error) {
    console.log(error);
  }
};

const buildList = async () => {
  try {
    const pizzaList = await getPizzas();
    pizzaList.forEach((pizza) => {
      let div = document.createElement("div");
      div.innerHTML = `<div class="card" style="width: 18rem;">
  <img src="${pizza.pictureUrl}" class="card-img-fluid" alt="...">
  <div class="card-body">
    <h5 class="card-title">${pizza.name}</h5>
    <p class="card-text">${pizza.description}</p>
  </div>
</div>`;
      contentElement.append(div);
    });
  } catch (error) {
    console.log(error);
  }
};

buildList();
