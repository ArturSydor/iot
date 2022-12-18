#!/bin/bash
set -e
while getopts u:f: flag; do
  case "${flag}" in
  u) base_url=${OPTARG} ;;
  f) template_file=${OPTARG} ;;
  *)
    printf "Unknown flag \n"
    exit 1
    ;;
  esac
done

crete_template() {
  file="$1"
  file_name="$(echo "${file}" | cut -d "." -f 1)"
  printf "Creating %s\n" "$file_name"
  curl --location --request PUT "${base_url}/_index_template/${file_name}" \
    --header "Content-Type: application/json" \
    --data-raw "$(cat "${file}")"
  printf "\nCreated %s\n\n" "$file_name"
}

if test -z "$template_file"; then
  arr=(*.json)
  echo "Indices count: ${#arr[@]}" # will echo number of elements in array
  printf "Indices template files: \n"
  echo "${arr[@]}" # will dump all elements of the array
  printf "\n"

  for counter in ${!arr[*]}; do
    crete_template "${arr[counter]}"
  done
else
  crete_template "${template_file}"
fi
