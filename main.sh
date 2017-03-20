#!/bin/bash

#%============================================================================
#%
#% FICHIER: main.sh
#%
#% UTILISATION: ./main.sh [-options]
#%
#% DESCRIPTION: Script de lancement du modéle et des plots
#% 
#% COMMANDES
#% D'AIDE   : ./main.sh -h (ouvre le fichier d'aide de run.sh)
#%
#%============================================================================

source $PWD/cf_commonFunctions.sh

default_pictPath="$PWD/pict"
default_dataFile="$PWD/data.txt"

run="false"
plot="false"

#----------------------------------------------------------------------
# affichage de l'aide si aucun paramétre entré
#----------------------------------------------------------------------
[[ $# = 0 ]] && cf_displayHelp $BASH_SOURCE -header_only && exit 0

while (( "$#" )); do
    case "$1" in
        -help|-h)
            #-----------------------------------------------------------------------
            #~ Affiche l'aide 
            #-----------------------------------------------------------------------
            echo " "  
            cf_displayHelp $BASH_SOURCE -comments -header
            exit 0
            ;;
        -list_sys|-ls)
            #-----------------------------------------------------------------------
            #~ Liste les données disponibles
            #-----------------------------------------------------------------------
            echo -e "\nListe des données disponibles :" 
            echo "$(head -n 1 $default_dataFile | sed 's/,/\n/g' | sed -E 's/_[0-9]+_//g' | sort -u)" 
            echo
            echo "$(head -n 1 $default_dataFile | sed 's/[a-zA-Z,]//g' | sed -E 's/_+/_/g' | sed -E 's/(^_[0-9]+).*([0-9]+_$)/\1-\2/g')" 
            echo
            exit 0
            ;;
        -run|-r)
            #-----------------------------------------------------------------------
            #~ Lancement le modele
            #~ Doit être suivi de 2 nombres entiers positifs (nb iteration et population)
            #-----------------------------------------------------------------------
            iter=$2
            cf_killIfNotPositiveInteger $iter
            shift
            pop=$2
            cf_killIfNotPositiveInteger $pop
            shift
            run="true"
            ;;
        -plot|-p)
            #-----------------------------------------------------------------------
            #~ Lancement du plot
            #~ Peut être suivi de clefs de filtrage (ex : Id env 0)
            #-----------------------------------------------------------------------
            plot="true"
            ;;
        #-nodisp)
            #    #-----------------------------------------------------------------------
            #    #~ Bloque l'affichage des images
            #    #-----------------------------------------------------------------------
            #    cfg_graph_opt="$cfg_graph_opt -nodisp"
            #    ;;
        #-save_pict=*|-sp=*)
            #    #-----------------------------------------------------------------------
            #    #~ Sauvegarde les images dans le dossier entré en paramétre
            #    #~ Dossier temporaire aussinon
            #    #-----------------------------------------------------------------------
            #    pictPath=$(find $PWD/${1#*=} -type d)
            #    if [[ ! -d $pictPath ]] 
            #    then
            #        echo -e "\terror:\t$pictPath not existing"
            #        exit 1
            #    fi
            #    ;;
        #-save_pict|-sp)
            #    #-----------------------------------------------------------------------
            #    #~ Sauvegarde les images dans le dossier d'image par défault
            #    #~ Dossier temporaire aussinon
            #    #-----------------------------------------------------------------------
            #    pictPath=$default_pictPath'/'$(date +%Y%m%d_%H%M%S)
            #    if [[ ! -d $pictPath ]] 
            #    then
            #        echo -e "\terror:\tdefault folder $pictPath not existing"
            #        exit 1
            #    fi
            #    ;;
        -*)
            echo "$1 : unknown option for $0"
            exit 1
            ;;
        *)
            keys=$keys" "$1
            ;;
    esac
    shift
done

echo " "  

# Lancement du modéle
if [[ $run != false ]]; then
    javac Model.java Population.java Individual.java Sigmoid.java Influences.java AbstractPractice.java IdealPractice.java NormPractice.java RealPractice.java Economy.java Identity.java AlternativePractice.java References.java Evaluation.java Percentage.java
    if [[ $? != 0 ]]; then
        exit 0
    fi

    ( cd ../.. && java model.basic_conv.Model $iter $pop > model/basic_conv/aef.dat )
    #( cd ../.. && java model.basic_conv.Model $iter $pop )

fi

# Filtrage des données
listData=$(head -n 1 $default_dataFile | sed 's/,/ /g' | sort -u)
for key in $keys; do
    echo "key = $key" 
    listData=$(echo "$listData" | tr ' ' '\n' | grep "$key" | tr '\n' ' ')
done


# Plot
if [[ $plot != false ]]; then
    echo "listData = $listData" 
    python plot.py $listData &
fi




