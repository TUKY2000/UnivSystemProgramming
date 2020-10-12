package com.tukY.Lab2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.*;


public class FiniteStateMachine {
    //region Fields
    State state;
    Map<Integer, State> states;

    int count_alphabet;
    int count_states;
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
    }
    //endregion


    //region Methods

    public State getCurrentState() {
        return state;
    }

    public Map<Character, State> getPossibleTransitionMap() {
        return state.transitions;
    }

    public CharSequence getPossibleTransition() {
        return getPossibleTransitionMap()
                .keySet()
                .toString()
                .substring(1, getPossibleTransitionMap().keySet().size() - 1)
                .replaceAll(", ", "");
    }

    public void exec(String word){
        Stream.of(word).forEach(this::exec);
    }

    public void exec(char c) throws InvalidParameterException{
        if(!isPossible(c))
            throw new InvalidParameterException("Current state has not transition : " + c);

        state = state.exec(c);
    }

    public void exec(CharSequence word){
        Stream.of(word).forEach(this::exec);
    }

    public boolean isPossible(char s){
        return state.transitions.containsKey(s);
    }

    public boolean isPossible(CharSequence word){
        var machine_buff = new FiniteStateMachine(this);

        try {
            for (char c : word.toString().toCharArray())
                machine_buff.exec(c);
        } catch (InvalidParameterException e){
            System.out.print(e.getMessage());
            return false;
        } catch (Exception e){
            System.out.print(e.getMessage());
        }
        return true;
    }

    //endregion
    //region Private Functions
    private void readMachine(BufferedReader reader) throws IOException {
        count_alphabet = Integer.getInteger(reader.readLine());
        count_states = Integer.getInteger(reader.readLine());

        state = new State(Integer.getInteger(reader.readLine()));

        // read final states
        var fstates = Arrays.stream(reader.readLine().split(" "))
                .skip(1)
                .map(Integer::getInteger)
                .collect(Collectors.toSet());

        states = getStates(reader, fstates);
    }

    private Map<Integer, State> getStates(BufferedReader reader, Set<Integer> fstates)
            throws IOException, InvalidParameterException {
        Map<Integer, State> states = new HashMap<>();
        String str;
        String[] trfun;

        State state0
            , state1;

        char transition;
        while (!(str = reader.readLine()).isEmpty()){   // mb i need to CHANGE
            trfun = str.split(" ");

            if (trfun[1].length() != 1)
                throw new InvalidParameterException("Invalid transition function : " + trfun[1]);

            state0 = states.computeIfAbsent(Integer.getInteger(trfun[0])
                    , key -> new State(key, fstates.contains(key)));
            state1 = states.computeIfAbsent(Integer.getInteger(trfun[2])
                    , key -> new State(key, fstates.contains(key)));

            transition = trfun[1].charAt(0);
            state0.addTransition(transition, state1);
        }
        return states;
    }
    //endregion

    //region Inner Classes
    public static class State{
        //region Fields
        private final int state;
        private final boolean isFinal;
        private final Map<Character, State> transitions;
        //endregion

        //region Constructors
        public State(int state, boolean isFinal){
            this.state = state;
            this.isFinal = isFinal;
            this.transitions = new HashMap<>();
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

        public State exec(char s){
            return transitions.getOrDefault(s, null);
        }

        public void addTransition(char transition, State state){
            transitions.putIfAbsent(transition, state);
        }
        //endregion
    }
    //endregion
}
