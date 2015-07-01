David Weinstein

Uncompleted Parts of the Assignment:
I haven't implemented a gui yet.
My commenting and testing is not extensive (I did them as needed rather than as ideal).

Known bugs:
When I use genesis as the corpus, I can't get Gomorrah back as a correction. I'm not sure what the bug behind that problem is.


Design details specific to your code:
Levenshtein, Trie, BigramMonogram, and Autocorrect are in their own packages and are intended to be decoupled. 

In my Trie, each every node (letter) is a trie that has an attribue that stores whether or not it's the end of a word.

BigramMonogram stores two maps: one of strings to lists of the words that have followed them and one that stores Strings to their frequencies. Arguably this functionality should be split, but the monogram functionality feels too trivial to separate into its own class.

Main runs the run parameters through CommandLineOptions (which is in the autocorrect package), which outputs the autocorrector that matches the users specifications. The autocorrector runs correct on the last word of the user's input. Correct generates and picks suggestions based on autocorrect's attributes (which were set by CommandLineOptions).


Any runtime/space optimizations you made beyond the minimum requirements:
While I have one Levenshtein distance method I wrote (albiet largely guided by Wikipedia, though I added the use of a map to store intermediate results), I use another distance method that I essentially copied from Wikipedia because it's faster. 
Trie doesn't have the optimization discussed in the assignment. 


How to run any tests you wrote/tried by hand:
The tests I wrote should be run by mvn package.


How to build/run your program from the command-line:
It can be run by ./run -smart -led 2 -prefix -whitespace -filename /home/d/Genesis.txt 
The order of the options doesn't matter.


What your smart suggestion does and how it improves our current suggestion:
My smart ordering is simply picking a correction with a Levenshtein distance of 1. If there isn't one, I used the normal process. A better solution would be to use Damerau–Levenshtein distance so that a transposition only contributes a distance of 1 rather than a distance of 2.



Design Question 1:
How would you change your frontend/backend code so that you could handle autocorrecting multiple input fields on the same page? Would you need to make any changes?

I haven't written by frontend yet, but I don't see why I'd have to change the backend at all. 


Design Question 2:
Suppose some new letter, θ, has been introduced into the English alphabet. This letter can be appended to the end of any English word, to negate it. For example, badθ would mean “good". One effect of this 1984-esque enhanced vocabulary is that you will now need to store twice as many words in your trie as before. How many more nodes will you need to store in your trie? Support your answer with details about how your trie is implemented and what data it stores.

The trie is implemented such that each letter is its own trie. For example, inputing the word "test" would requiring inputing four tries (assuming no relevant tries had already been inserted). To input "testθ" would require one additional trie. 
To store all the new words, I couldn't just add one new Trie that all the words point to becasue each of my tries stores their parent. As such, I would need on new trie per word (note that that is different from per letter).

