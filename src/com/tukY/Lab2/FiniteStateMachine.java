package com.tukY.Lab2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.stream.Collectors;
import java.util.*;

public class FiniteStateMachine {
    //region Fields
    private State state;
    private Map<Integer, State> states;
    private String alphabet;

    private int count_alphabet;
    private int count_states;
    //endregion

    //region Constructors

    public FiniteStateMachine(String filepath){
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))){
            readMachine(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  copy constructor
    public FiniteStateMachine(FiniteStateMachine machine){
        this.state = machine.state;
        this.states = machine.states;
        this.count_states = machine.count_states;
        this.count_alphabet = machine.count_alphabet;
        this.alphabet = machine.alphabet;
    }

    private FiniteStateMachine(FiniteStateMachine machine, State state) {
        this.state = state;
        this.states = machine.states;
        this.count_states = machine.count_states;
        this.count_alphabet = machine.count_alphabet;
        this.alphabet = machine.alphabet;
    }
    //endregion


    //region Methods

    public State getCurrentState() {
        return state;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public List<FiniteStateMachine> exec(char c) throws InvalidParameterException{
        if(!isPossible(c))
            throw new InvalidParameterException("Current state has not transition : " + c);

        return state.exec(c).stream()
                .map(s -> new FiniteStateMachine(this, s))
                .collect(Collectors.toList());
    }

    public List<FiniteStateMachine> exec(String word){
        List<FiniteStateMachine> res = new ArrayList<>();

        if (!word.isEmpty())
            if (isPossible(word.charAt(0)))
                res.addAll(exec(word.substring(1)));

        return res;
    }

    public boolean isPossible(char c){
        return state.transitions.stream()
                .anyMatch(tfn -> tfn.getTransitionFun() == c);
    }

    public boolean isPossible(String word){
        if (word.isEmpty())
            return true;

        if (!isPossible(word.charAt(0)))
            return false;

        return exec(word.charAt(0)).stream()
                .anyMatch(fsm -> fsm.isPossible(word.substring(1)));
    }

    //endregion
    //region Private Functions
    private void readMachine(BufferedReader reader) throws IOException {
        count_alphabet = Integer.parseInt(reader.readLine());
        count_states = Integer.parseInt(reader.readLine());

        int start = Integer.parseInt(reader.readLine());

        // read final states
        Set<Integer> fstates = Arrays.stream(reader.readLine().split(" "))
                .skip(1)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());

        states = getStates(reader, fstates);

        state = states.get(start);
    }

    private Map<Integer, State> getStates(BufferedReader reader, Set<Integer> fstates)
            throws IOException, InvalidParameterException {
        Map<Integer, State> states = new HashMap<>();

        String str;
        String[] tfn;

        State state0
            , state1;

        alphabet = "";
        String transition;
        while ((str = reader.readLine()) != null){
            tfn = str.split(" ");

            for (String c : tfn[1].split(""))
                if (!alphabet.contains(c))
                    alphabet = alphabet.concat(c);

            if (alphabet.length() > count_alphabet)
                throw new InvalidParameterException("Invalid FSM description");

            state0 = states.computeIfAbsent(Integer.valueOf(tfn[0])
                    , key -> new State(key, fstates.contains(key)));
            state1 = states.computeIfAbsent(Integer.valueOf(tfn[2])
                    , key -> new State(key, fstates.contains(key)));

            transition = tfn[1];
            state0.addTransition(transition, state1);
        }
        return states;
    }
    //endregion

    //region Inner Classes

    public static class Transition{
        private final char tfn;
        private final State toState;

        public Transition(char tfn, State toState) {
            this.tfn = tfn;
            this.toState = toState;
        }

        public char getTransitionFun() {
            return tfn;
        }

        public State getState() {
            return toState;
        }
    }

    public static class State{
        //region Fields
        private final int state;
        private final boolean isFinal;
        private final Set<Transition> transitions;
        //endregion

        //region Constructors
        public State(int state, boolean isFinal){
            this.state = state;
            this.isFinal = isFinal;
            this.transitions = new HashSet<>();
        }

        public State(int state){
            this(state, false);
        }
        //endregion

        //region Methods
        public int state(){
            return state;
        }

        public boolean isFinal(){
            return isFinal;
        }

        public List<State> exec(char c){
            return transitions.stream()
                    .filter(tfn -> tfn.getTransitionFun() == c)
                    .map(Transition::getState)
                    .collect(Collectors.toList());
        }

        public void addTransition(char transition, State state){
            transitions.add(new Transition(transition, state));
        }

        public void addTransition(String transition, State state){
            for (char c : transition.toCharArray())
                addTransition(c, state);
        }
        //endregion
    }
    //endregion
}
