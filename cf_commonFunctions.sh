#!/bin/bash

#=== FUNCTION ================================================================
# NOM: cf_displayHelp
# DESCRIPTION: liste les options d'un fichier sh (lignes commencant par `-` et finissant par `)`)
# PARAMETRE 1: Chemin du fichier sh à traiter
#===========================================================================
cf_displayHelp(){
    local filePath=$1

    #----------------------------------------------------------------------
    # Par défault, n'affiche que les options
    #----------------------------------------------------------------------
    local selectOpt="^\s*[-~].*)"
    local removeMark="s/#[\~\%]/  /g"
    local removeLastPatenthesis="s/^\(\s*[~-].*\))/\1/g"
    local removeDefaultOpt="s/^\s*-\*)\s*//g"

    while (( "$#" )); do
        case "$1" in
            -comments|-c)
                local selectCom="\|^\s*#~.*$"
                local removeSpace="s/^\s\+/ /g"
                local addEolBeforeOpt="s/^\s*[~-]/\n&/g"
                ;;
            -header)
                local selectHead="\|^\s*#%.*$"
                ;;
            -header_only|-ho)
                local selectHead="^\s*#%.*$"
                local selectOpt=""
                local removeDefaultOpt=""
                ;;
        esac
        shift
    done

    grep "$selectOpt$selectCom$selectHead" $filePath | sed -e "$removeDefaultOpt;$removeSpace;$addEolBeforeOpt;$removeMark;$removeLastPatenthesis"
}


#=== FUNCTION ================================================================
# NOM: cf_killIfNotPositiveInteger
# DESCRIPTION: Teste si la chaine de caractéres est un nombre entier positif
# PARAMETRE 1: chaine de caractéres à tester
#===========================================================================
cf_killIfNotPositiveInteger(){
    local stringToTest=$1
    local int=^[0-9]+$
    if [[ ! $stringToTest =~ $int ]] 
    then
        echo
        echo "Impossible to have $stringToTest here"
        echo "Must be a positive integer... Try again"
        echo
        exit 1
    fi
}
