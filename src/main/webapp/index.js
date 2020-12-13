let baseUrl = window.location.toString();

console.log(baseUrl);

const rootUrl = baseUrl.concat('webapi');

window.onload = () => {
    let fetchedIngredients = [];
    let fetchedTags = [];
    let JwtToken = '';

    /* Ingredient fields */
    const ingredientNameInput = document.getElementById('ingredient-name');
    const ingredientUnitInput = document.getElementById('ingredient-unit');
    const ingredientPortionInput = document.getElementById('ingredient-amount');
    const ingredientPriceInput = document.getElementById('ingredient-price');
    const ingredientButton = document.getElementById('ingredient-button');
    ingredientButton.addEventListener('click', handleSubmitIngredient);

    /* Tag fields */
    const tagNameInput = document.getElementById('tag-name');
    const tagButton = document.getElementById('tag-button');
    tagButton.addEventListener('click', handleSubmitTag);

    /* Recipe fields */
    const recipeNameInput = document.getElementById('recipe-name');
    const recipePortionsInput = document.getElementById('recipe-portions');

    const loginButton = document.getElementById('login-button');
    loginButton.addEventListener('click', handleLogin);

    const responseStatus= document.getElementById("response-status");
    const responseBody= document.getElementById("response-body");

    const recipeIngredientInputs = [];

    for (let index = 0; index < 5; index++) {
        const select = document.getElementById(`recipe-ingredient-${index + 1}`);
        const amount = document.getElementById(`recipe-ingredient-${index + 1}-amount`);
        const unit = document.getElementById(`recipe-ingredient-${index + 1}-unit`);

        recipeIngredientInputs.push({ select, amount, unit });

        select.addEventListener('change', handleSelectIngredient);
    }

    const recipeTags = document.getElementById('recipe-tags');

    const recipeTagInputs = [];
    for (let index = 0; index < 3; index++) {
        const input = document.getElementById(`recipe-tag-${index + 1}`);
        recipeTagInputs.push(input);
    }

    const recipeButton = document.getElementById('recipe-button');
    recipeButton.addEventListener('click', handleSubmitRecipe);

    /* Fetch data to fill fields */
    const fetchTags = () => {
        fetch(rootUrl + '/tags', {
            headers: {
                'Accept': 'application/json'
            },
        }).then(response => response.json())
            .then(response => {
                console.log("fetched: ", response);
                fetchedTags = [...response];

                while (recipeTags.childElementCount > 0) {
                    recipeTags.removeChild(recipeTags.lastElementChild);
                }

                fetchedTags.forEach(tag => {
                    const opt = document.createElement('option');
                    opt.value = tag.name;
                    recipeTags.appendChild(opt);
                })
            })
            .catch(error => console.log(error))
    }

    const fetchIngredients = () => {
        fetch(rootUrl + '/ingredients', {
            headers: {
                'Accept': 'application/json'
            },
        }).then(response => response.json())
            .then(response => {
                console.log("fetched: ", response);
                fetchedIngredients = [...response];

                recipeIngredientInputs.forEach(input => {
                    const select = input.select;
                    while (select.childElementCount > 1) {
                        select.removeChild(select.lastElementChild);
                    }

                    fetchedIngredients.forEach(ingredient => {
                        const opt = document.createElement('option');
                        opt.text = ingredient.name;
                        opt.value = ingredient.id;
                        select.appendChild(opt);
                    })
                })
            }).catch(error => console.log(error));

    }

    /* Event handlers*/
    function handleLogin(event) {
        fetch(rootUrl + '/auth', {
            method: 'POST',
            headers: {
                'Authorization': 'Basic dXNlcjpwYXNzd29yZA=='
            },
        }).then(response => {
            responseStatus.textContent = response.status;
            console.log(response.headers)
            responseBody.textContent = '';
                JwtToken = response.headers.get('Authorization').replace('Bearer ', '');
                console.log("token: ", JwtToken);
                return response.json();
            })
            .catch(error => console.log(error))
    }

    function handleSelectIngredient(event) {
        const ingredient = fetchedIngredients.filter(ingredient => {
            return ingredient.id == event.target.value;
        })[0];

        const id = event.target.id;
        const unitField = document.getElementById(id + '-unit');
        if (ingredient) {
            unitField.textContent = ingredient.unit;
        } else {
            unitField.textContent = '';
        }
    }

    function handleSubmitTag(event) {
        const tag = {};
        tag.name = tagNameInput.value;

        fetch(rootUrl + '/tags', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + JwtToken,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(tag)
        }).then(response => {
            responseStatus.textContent = response.status;
            return response.json()})
            .then(response => {
                responseBody.textContent = JSON.stringify(response, null, 2);
                console.log("created: ", response);
                tagNameInput.value = '';
                fetchTags();
            })
            .catch(error => console.log(error))
    }

    function handleSubmitIngredient(event) {
        const ingredient = {};
        ingredient.name = ingredientNameInput.value;
        ingredient.unit = ingredientUnitInput.value;
        ingredient.portionSize = ingredientPortionInput.value;
        ingredient.portionPrice = ingredientPriceInput.value;

        fetch(rootUrl + '/ingredients', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + JwtToken,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(ingredient)
        }).then(response => {
            responseStatus.textContent = response.status;
            return response.json()})
            .then(response => {
                responseBody.textContent = JSON.stringify(response, null, 2)
                console.log("created: ", response);
                fetchIngredients();
                clearInputs();
            })
            .catch(error => console.log(error))

        function clearInputs() {
            ingredientNameInput.value = '';
            ingredientUnitInput.value = '';
            ingredientPortionInput.value = '';
            ingredientPriceInput.value = '';
        }
    }

    function handleSubmitRecipe(event) {
        const recipe = {};
        recipe.name = recipeNameInput.value;
        recipe.portions = recipePortionsInput.value;
        recipe.ingredients = [];
        recipe.tags = [];

        recipeIngredientInputs.forEach(input => {
            const id = input.select.value;
            if (input.select.value != -1) {
                const ingredient = fetchedIngredients.filter(ingredient => ingredient.id == input.select.value)[0];
                recipe.ingredients.push({
                    ingredient: ingredient,
                    amount: input.amount.value / ingredient.portionSize
                })
            }
        })

        recipeTagInputs.forEach(input => {
            if (input.value != '') {
                const tag = fetchedTags.filter(tag => tag.name == input.value)[0];
                recipe.tags.push(tag)
            }
        })

        console.log('posting...', recipe);

        fetch(rootUrl + '/recipes', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + JwtToken,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(recipe)
        }).then(response => {
            responseStatus.textContent = response.status;
            return response.json()})
            .then(response => {
                responseBody.textContent = JSON.stringify(response, null, 2)
                console.log("created: ", response);
                clearInputs();
            })
            .catch(error => console.log(error))

        function clearInputs() {
            recipeNameInput.value = '';
            recipePortionsInput.value = '';

            recipeIngredientInputs.forEach(input => {
                input.select.value = -1;
                input.amount.value = '';
                input.unit.textContent = '';
            })

            recipeTagInputs.forEach(input => {
                input.value = '';
            })
        }
    }

    fetchIngredients();

    fetchTags();
}