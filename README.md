# 2048
A well-built Android app of the classic 2048 game.

<img src="https://github.com/aryanA101a/2048/assets/23309033/f9a70457-ab75-4957-9e0b-de0844a52b9a" width="20%">

## Milestones 🚩
- [x] Basic functionality
- [x] Complete functionality and local persistence

## Project Structure 🧬
```
├── adapter
│   └── GridAdapter.kt
├── data
│   ├── model
│   │   ├── Board.kt
│   │   ├── Cell.kt
│   │   ├── Game.kt
│   │   ├── Move.kt
│   │   ├── SavedGame.kt
│   │   └── SavedGameSerializer.kt
│   └── repository
│       └── GameRepository.kt
├── di
│   └── AppModule.kt
├── MainActivity.kt
├── MyApp.kt
├── util
│   └── Util.kt
├── view
│   ├── fragment
│   │   ├── GameFragment.kt
│   │   └── WinningFragment.kt
│   └── listener
│       └── OnSwipeTouchListener.kt
└── viewmodel
    └── GameViewModel.kt
```

## Dependencies 🖇️
1. Hilt (Dependency Injection)
2. Proto Datastore (Typesafe local persistence)

## Contributing 🎉
Pull requests are welcome.  
- Please make sure to follow the development style.  
- For major changes, please open an issue first, and discuss, what you would like to change.

