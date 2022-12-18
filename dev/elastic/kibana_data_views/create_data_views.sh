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

crete_data_view() {
  file="$1"
  file_name="$(echo "${file}" | cut -d "." -f 1)"
  printf "Creating %s\n" "$file_name"
  curl --location --request POST "${base_url}/api/data_views/data_view" \
    --header "kbn-xsrf: true" \
    --header "Content-Type: application/json" \
    --data-raw "$(cat "${file}")"
  printf "\nCreated %s\n\n" "$file_name"
}

if test -z "$template_file"; then
  arr=(*.json)
  echo "Data views count: ${#arr[@]}" # will echo number of elements in array
  printf "Data views template files: \n"
  echo "${arr[@]}" # will dump all elements of the array
  printf "\n"

  for counter in ${!arr[*]}; do
    crete_data_view "${arr[counter]}"
  done
else
  crete_data_view "${template_file}"
fi
