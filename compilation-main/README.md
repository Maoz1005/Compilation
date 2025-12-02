# Compilation Project! - Neri, Rotem
## Emails:
1. rotemtripo@gmail.com
2. neri.nigberg@gmail.com

## EX3
### Attempted summary:
#### General Description:
1. In this assignment we're required to check for the validity of the function. This means enforce correct typing, return statements, valid values, valid inheritance, existence of variables, etc' etc'.
2. Some instructions attempt to simplify the task, so it's important to note what is necessary, and what isn't.
3. I'd imagine the toughest part is to implement the whole logic for scoping and valid inheritance.
4. I've added "TODO's" for questions I don't know the answer to.

#### Types
1. General
   - Two primitives - Strings and Integers 
   - "...defining classes and arrays **is only possible in the uppermost (global) scope.**" (Best to look at examples at the end of the assignement, especially when it comes to arrays.) 
   - "void" can only be a return type in a function declaration.
      
2. Classes
   - "They can refer to/extend only previously defined classes"
   - A method can't refer to a method defined after it.
   - "Following the same concept, a method M can’t refer to a data member d, if d is defined after M in the class"
   - Methods overloading is illegal **except** for in extending subclasses.
   - "Similarly, it is illegal to define a variable with the same name of a previously defined variable (shadowing)
   - "nil" is allowed to be passed instead of object types
      
3. Arrays 
   - Reminder: Array types can only be defined in the uppermost (global) scope (Not intuitive!)
   - The rest is exactly like Java pretty much
   - TODO: WTF does "Interchangable" mean??
   - Arrays can also be nil.

#### Assignments
1. General
   - Assigning "nil" to integers **and strings** is **illegal**
   - TODO: Father x = Son y is legal , but what about upcasting? 
   - "a declared data member inside a class can be initialized only with a constant value that matches its type." (Example: int i := 42) _"Table 7 summarizes these facts"_
   
#### If and While statements
1. General
   - TODO: The type of the condition inside if and while statements is the primitive type int." But what about "Father.age > Son.age"?

#### Return Statement
1. General
   - It's just basic function syntax. Nothing fancy. (return types must match, void functions return statement should be empty...)
   - However, even if a function isn't void - not all paths must end with a return statement. (See example. kind of weird)

#### Equality Testing
1. General
   - Equality "x = y" (notice, not double equals!) returns a primitive int.
   - x and y must be of the same type or have an inheritance relationship. (TODO: I think it has to be grandfather-grandson though)
   - ^ Note that any type **except string** can be compared with nil (TODO: what about integers?)

#### Binary Operations
1. General
   - Binary operators only work on integers, and returns integers.
   - With the exceptions "+" also works on strings, where the result is a string.
   - Notice the divisor cannot be 0.

#### Scope rules
1. General - TODO


### TODO:
1. ~~Pretty obvious we have to implement scoping logic (question is, how?)~~
2. Create visitor pattern for Neri's AST, so we can traverse and add logic through it (recitation 6 detailed this apparently)
3. Documentation of Neri's AST (Struggling to understand relationship between all classes :/)
4. Add more

### MORE TODO:
1. SemantMe() if primitives - what do we need to add here? Unclear
2. What is "TYPE_FOR_SCOPE_BOUNDARIES"?
3. How does "TYPE_LIST" work? Why aren't we just chaining types to one another?

### Useful files:
1. src/TYPES/*.java - TODO: I... don't get what this does. I thought all of the data already exists on the AST itself? 
2. src/SYMBOL_TABLE/*.java - TODO: Why is this superior over a regular hash table? wtf
3. Add more as you see fit





## EX2
### Relevant code files:
1. <ins>cup/CUP_FILE.cup</ins> - Derivation rules, error function, terminals, non terminals, are all defined here.
2. <ins>jflex/LEX_FILE.lex</ins> - Basically the lex file from ex1 integrated with cup. I don't think this needs to be touched.
3. <ins>src/Main.java</ins> - Needs no introduction.
4. For AST - <ins>src/AST/*</ins> - All AST node types are defined here, as well as the data within them.

### Useful information:
Mo' Derivation rules Mo' problems.

### TODO:
I'm not entirely sure honestly, but just off the top of my head:
1. Check derivation rules actually work and pass testers
2. ~~Implement write "OK" to file logic~~
3. ~~Implement error-throwing logic~~
4. Figure out if "output/ParseStatus.txt" is the file we're supposed to write to
5. Neri - AST
6. Bonus points: Find a tester and see that everything passes.

## EX1
### Relevant code files:
1. <ins>LEX_FILE.lex</ins> - Matches regular expressions and converts them to "Symbol" objects
2. <ins>TokenNames.java</ins> - Determines the ID number for a given token. Any token that will be written to file should have an ID.
3. <ins>Main.java</ins> - Defines the printing format and write-to-file format. (It also includes a small function to check a number is in range)

#### Useful information:
1. input.txt is set as the default file to read from when activating "make"
2. "make" only works about 1 out of 3 times when it's activated - if you encounter an error make sure its something coherent that has to do with your code, if not - try "make"ing again.

### TODO:
1. (FIXED ACCORDING TO DEKEL WITH BABY CHECKS BY ME) Most difficult (i think) - Regex a multiline comment ("type 2"): can't figure it out. People on WhatsApp mentioned "state" but idk
2. (LOOKS GOOD TO ME) Figure out the precedence of regex combinations. (for example: "void" should be matched as a keyword and not an ID)
3. (YUP) Figure out if we need a special way to handle lexicographic errors: "Whenever the input program contains a lexical error, the output file should contain a single word only: ERROR."
4. (TIS TRUE) Fix write-to-file format. (Easiest task I'd reckon)
5. Once everything is working - validate code matches requirements in PDF!

### TODO 2.0:
1. Problem with errors: Doesn't write to file under most circumstances according to tester:
   https://github.com/dinasim/compilation_hw1_tester
2. Unclosed T2 comment doesn't throw error like it should
   (Potential solution: match <EOF> token with T2 comment as a special token that throws an error if matched?)
3. T1 comment at EOF doesn't throw an error like it should
   (Potential solution: match <EOF> token with T2 comment as a special token that throws an error if matched?)