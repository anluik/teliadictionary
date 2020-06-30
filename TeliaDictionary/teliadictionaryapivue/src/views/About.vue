<template>
    <div>
        <br />
        <h3>About</h3>
        <hr />

        <p> Dictionary web application made by Andri Luik in summer 2020 for Telia software development intern position. </p>
        <p>
            On Home page you can search for term in the dictionary. <br />
            Use the search button to activate search and the switch button to change the search language. <br />
            Exact match to your result will be displayed in the 'Exact Match' section with the translation(s) and also
            examples after pressing button 'See examples'. <br />
            Similar matches are words in the dictionary that differ from the search word by 1 or 2 letter changes by inserting,
            replacing or deleting one.
            Maximum of 5 similar matches will be displayed at once. <br />
        </p>
        <p>
            The Dictionary page allows you to see all the words in the dictionary. <br />
            Navigate to desired letter by pressing it in the navigation letters section under the page title. <br />
            Add a new word with up to 6 translations and 2 examples to the dictionary by filling the form that opens
            after pressing 'Add to dictionary' button. You can add more translations and examples through the same form by
            inserting the already existing word as the new word. <br />
            Switch the language of the words by pressing the switch button next to the 'Add to dictionary' button. <br />
            Selecting a word in the list opens a dropdown with search and delete options. Search will navigate you to
            the home page and automatically search for the selected word. <br />
            To delete a translation link between two words, delete one of the words and create it again.
        </p>

    </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component
export default class WordsIndex extends Vue {
}
</script>
